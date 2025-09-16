package api.expenses.expenses.services.expenses.expensesFiles;

import api.expenses.expenses.enums.BanksEnum;
import api.expenses.expenses.enums.PaymentMethodEnum;
import api.expenses.expenses.helpers.PdfExtractprHelper;
import api.expenses.expenses.records.ExpenseToAdd;
import api.expenses.expenses.records.ParsedExpense;
import api.expenses.expenses.services.expenses.ExpenseIndividualService;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@RequiredArgsConstructor
public abstract class ExpenseFileStrategy {
    protected final ExpenseIndividualService expenseIndividualService;
    protected final List<PdfExtractprHelper> parsers;

    public abstract boolean match(BanksEnum banksEnum);
    public abstract BanksEnum getBank();
    public abstract PaymentMethodEnum getBankMethod();

    protected abstract BigDecimal resolveAmount(ParsedExpense e);

    public void process(String file) {
        var parser = parsers.stream()
                .filter(p -> p.match(getBank()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No parser found for " + getBank()));

        var expenses = parser.parse(file);

        var date = expenses.getLast().dateTime().toLocalDate();
        int year = date.getYear();
        int month = date.getMonthValue();

        for (ParsedExpense e : expenses) {
            int cuotaActual = 0;
            int cuotaTotales = 0;

            if (e.installment() != null && !e.installment().isEmpty()) {
                String[] parts = e.installment().split("/");
                cuotaActual = Integer.parseInt(parts[0]);
                cuotaTotales = Integer.parseInt(parts[1]);
            }

            ExpenseToAdd expenseToAdd = new ExpenseToAdd(
                    this.resolveAmount(e),
                    e.dateTime(),
                    e.reference(),
                    null,
                    this.getBankMethod().name(),
                    e.currency().getSymbol(),
                    cuotaActual,
                    cuotaTotales,
                    year,
                    month,
                    getBank()
            );
            expenseIndividualService.saveExpense(expenseToAdd);
        }
    }
}
