package api.expenses.expenses.helpers;

import api.expenses.expenses.enums.BanksEnum;
import api.expenses.expenses.enums.CurrencyEnum;
import api.expenses.expenses.records.AmountInfo;
import api.expenses.expenses.records.ParsedExpense;
import api.expenses.expenses.repositories.CurrencyRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Slf4j
public class GaliciaPdfExtractorHelper extends PdfExtractprHelper {

    private static final Pattern GALICIA_EXPENSE_PATTERN = Pattern.compile(
            "(\\d{2}-\\d{2}-\\d{2})\\s+" +
                    "(.+?)\\s+" +
                    "(\\d{6})\\s+" +
                    "([\\d.,]+)(?:\\s+([\\d.,]+))?"
    );
    private static final Pattern DATE_LINE_PATTERN = Pattern.compile("^\\d{2}-\\d{2}-\\d{2}.*");
    private static final Pattern REFERENCE_CLEANUP_PATTERN = Pattern.compile("^[*KV]");

    public GaliciaPdfExtractorHelper(CurrencyRepository currencyRepository) {
        super(currencyRepository);
    }

    @Override
    public boolean match(BanksEnum banksEnum) {
        return BanksEnum.GALICIA.equals(banksEnum);
    }

    @Override
    public List<ParsedExpense> parse(String pdfText) {
        return pdfText.lines()
                .parallel()
                .map(String::trim)
                .filter(this::isValidExpenseLine)
                .map(this::parseExpenseLine)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();
    }

    private boolean isValidExpenseLine(String line) {
        return !line.isEmpty() && DATE_LINE_PATTERN.matcher(line).matches();
    }

    private Optional<ParsedExpense> parseExpenseLine(String line) {
        Matcher matcher = GALICIA_EXPENSE_PATTERN.matcher(line);

        if (!matcher.find()) {
            return Optional.empty();
        }
        try {
            return Optional.of(this.buildParsedExpense(matcher));
        } catch (Exception e) {
            log.warn("Error parsing line: '{}' - {}", line, e.getMessage());
            return Optional.empty();
        }
    }

    private ParsedExpense buildParsedExpense(Matcher matcher) {
        var date = parseDate(matcher.group(1));
        var fullReference = matcher.group(2).trim();
        var comprobante = matcher.group(3);

        var installment = this.extractInstallment(fullReference);
        var cleanedReference = cleanReference(fullReference, installment.isPresent());

        var amounts = parseAmounts(matcher, fullReference);
        var currency = currencyRepository.findBySymbol(amounts.hasForeignCurrency() ? CurrencyEnum.USD.name() : CurrencyEnum.ARS.name())
                .orElseThrow(() -> new EntityNotFoundException("Currency not found"));

        return new ParsedExpense(
                date.atStartOfDay(),
                cleanedReference,
                installment.orElse(null),
                comprobante,
                currency,
                amounts.pesos(),
                amounts.dolares()
        );
    }

    private LocalDate parseDate(String dateStr) {
        return LocalDate.parse(dateStr, super.getDateFormat());
    }

    private Optional<String> extractInstallment(String reference) {
        Matcher installmentMatcher = INSTALLMENT_PATTERN.matcher(reference);
        return installmentMatcher.find()
                ? Optional.of(installmentMatcher.group(1))
                : Optional.empty();
    }

    private String cleanReference(String reference, boolean hasInstallment) {
        if (reference == null) return null;

        String cleaned = reference.trim();

        cleaned = REFERENCE_CLEANUP_PATTERN.matcher(cleaned).replaceFirst("");

        if (hasInstallment) {
            cleaned = cleaned.replaceAll("\\s*\\d{2}/\\d{2}\\s*", " ");
        }

        return cleaned.trim();
    }

    private AmountInfo parseAmounts(Matcher matcher, String fullReference) {
        boolean hasForeignCurrency = FOREIGN_CURRENCY_PATTERN.matcher(fullReference).find();

        BigDecimal pesos = null;
        BigDecimal dolares;

        if (hasForeignCurrency) {
            dolares = parseAmount(matcher.group(5)).orElse(parseAmount(matcher.group(4)).orElse(null));
        } else {
            pesos = parseAmount(matcher.group(4)).orElse(null);
            dolares = parseAmount(matcher.group(5)).orElse(null);
        }

        return new AmountInfo(pesos, dolares, hasForeignCurrency);
    }
}
