package api.expenses.expenses.services.expenses.typeExpense;

import api.expenses.expenses.entities.Credito;
import api.expenses.expenses.enums.PaymentMethodEnum;
import api.expenses.expenses.mappers.ExpenseMapper;
import api.expenses.expenses.records.ExpenseToAdd;
import org.springframework.stereotype.Service;

@Service
public class CreditExpenseStrategy extends ExpenseStrategy {


    public CreditExpenseStrategy(ExpenseMapper expenseMapper) {
        super(expenseMapper);
    }

    @Override
    public boolean match(PaymentMethodEnum paymentMethod) {
        return PaymentMethodEnum.CREDITO.equals(paymentMethod);
    }

    @Override
    public Credito process(ExpenseToAdd expenseToAdd) {
        return expenseMapper.toCredito(expenseToAdd);
    }
}
