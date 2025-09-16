package api.expenses.expenses.services.expenses.expensesFiles;

import api.expenses.expenses.enums.BanksEnum;
import api.expenses.expenses.helpers.PdfExtractprHelper;
import api.expenses.expenses.services.expenses.ExpenseIndividualService;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public abstract class ExpenseFileStrategy {
    protected final ExpenseIndividualService expenseIndividualService;
    protected final List<PdfExtractprHelper> parsers;

    public abstract boolean match(BanksEnum banksEnum);
    public abstract void process(String file);
}
