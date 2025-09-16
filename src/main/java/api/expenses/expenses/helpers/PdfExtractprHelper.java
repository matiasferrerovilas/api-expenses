package api.expenses.expenses.helpers;

import api.expenses.expenses.enums.BanksEnum;
import api.expenses.expenses.enums.CurrencyEnum;
import api.expenses.expenses.records.ParsedExpense;
import api.expenses.expenses.repositories.CurrencyRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@Slf4j
@RequiredArgsConstructor
public abstract class PdfExtractprHelper {

    protected final CurrencyRepository currencyRepository;
    protected static final Pattern FOREIGN_CURRENCY_PATTERN = Pattern.compile("(CHF|USD|EUR)\\s+[\\d.,]+");
    protected static final Pattern INSTALLMENT_PATTERN = Pattern.compile("(\\d{2}/\\d{2})");

    public DateTimeFormatter getDateFormat() {
        return DateTimeFormatter.ofPattern("dd-MM-yy");
    }
    public BigDecimal parseMoney(String amount) {
        if (amount == null || amount.trim().isEmpty()) {
            return null;
        }

        String cleaned = amount.trim()
                .replace(".", "")
                .replace(",", ".");

        return new BigDecimal(cleaned);
    }
    public Optional<BigDecimal> parseAmount(String amountStr) {
        if (amountStr == null || amountStr.trim().isEmpty()) {
            return Optional.empty();
        }

        try {
            return Optional.of(parseMoney(amountStr.trim()));
        } catch (Exception e) {
            log.debug("Failed to parse amount: '{}'", amountStr);
            return Optional.empty();
        }
    }
    public abstract List<ParsedExpense> parse(String pdfText);
    public abstract boolean match(BanksEnum banksEnum);
}