package api.expenses.expenses.services.expenses.expensesFiles;

import api.expenses.expenses.enums.BanksEnum;
import api.expenses.expenses.enums.CurrencyEnum;
import api.expenses.expenses.enums.PaymentMethodEnum;
import api.expenses.expenses.helpers.PdfExtractprHelper;
import api.expenses.expenses.records.ExpenseToAdd;
import api.expenses.expenses.records.ParsedExpense;
import api.expenses.expenses.services.expenses.ExpenseIndividualService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@Slf4j
public class BBVACreditImportService extends ExpenseFileStrategy {

    public BBVACreditImportService(ExpenseIndividualService expenseIndividualService, List<PdfExtractprHelper> parsers) {
        super(expenseIndividualService, parsers);
    }

    @Override
    public boolean match(BanksEnum banksEnum) {
        return BanksEnum.BBVA.equals(banksEnum);
    }

    @Override
    public void process(String file) {
        var parserList = parsers.stream()
                .filter(parser -> parser.match(BanksEnum.BBVA))
                .toList();

        var expenses = switch (parserList.size()) {
            case 0 -> throw new IllegalArgumentException("Invalid bank method");
            case 1 -> parserList.getFirst().parse(file);
            default -> throw new IllegalArgumentException("Multiple strategies found for bank method");
        };

        LocalDate date = expenses.getLast().dateTime().toLocalDate();
        int year = date.getYear();
        int month = date.getMonthValue();
        int cuotaActual = 0;
        int cuotaTotales = 0;

        for (ParsedExpense e : expenses) {
            BigDecimal amount = CurrencyEnum.USD.equals(CurrencyEnum.valueOf(e.currency().getSymbol())) ? e.amountDolares() : e.amountPesos();

            if (e.installment() != null && !e.installment().isEmpty()) {
                String[] parts = e.installment().split("/");
                cuotaActual = Integer.parseInt(parts[0]);
                cuotaTotales = Integer.parseInt(parts[1]);
            }
            ExpenseToAdd expenseToAdd = new ExpenseToAdd(amount,
                    e.dateTime(),
                    e.reference(),
                    null,
                    PaymentMethodEnum.CREDITO.name(),
                    e.currency().getSymbol(),
                    cuotaActual,
                    cuotaTotales,
                    year,
                    month,
                    BanksEnum.BBVA);
            expenseIndividualService.saveExpense(expenseToAdd);
        }
    }
}
