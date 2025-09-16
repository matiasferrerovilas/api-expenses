package api.expenses.expenses.services.expenses.typeExpense;

import api.expenses.expenses.entities.Gasto;
import api.expenses.expenses.enums.PaymentMethodEnum;
import api.expenses.expenses.mappers.ExpenseMapper;
import api.expenses.expenses.records.ExpenseToAdd;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class ExpenseStrategy {
    protected final ExpenseMapper expenseMapper;

    public abstract boolean match(PaymentMethodEnum paymentMethod);
    @Transactional
    public abstract Gasto process(ExpenseToAdd expenseToAdd);
}
