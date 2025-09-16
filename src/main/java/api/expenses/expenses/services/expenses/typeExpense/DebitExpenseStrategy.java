package api.expenses.expenses.services.expenses.typeExpense;

import api.expenses.expenses.entities.Debito;
import api.expenses.expenses.enums.PaymentMethodEnum;
import api.expenses.expenses.mappers.ExpenseMapper;
import api.expenses.expenses.records.ExpenseToAdd;
import org.springframework.stereotype.Service;

@Service
public class DebitExpenseStrategy extends ExpenseStrategy {

    public DebitExpenseStrategy(ExpenseMapper expenseMapper) {
        super(expenseMapper);
    }

    @Override
    public boolean match(PaymentMethodEnum paymentMethod) {
        return PaymentMethodEnum.DEBITO.equals(paymentMethod);
    }

    @Override
    public Debito process(ExpenseToAdd expenseToAdd) {
        return expenseMapper.toDebito(expenseToAdd);
    }
}
