package api.expenses.expenses.constrains;

import api.expenses.expenses.enums.PaymentMethodEnum;
import api.expenses.expenses.records.ExpenseToAdd;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CuotasValidator implements ConstraintValidator<ValidCuotas, ExpenseToAdd> {

    @Override
    public boolean isValid(ExpenseToAdd value, ConstraintValidatorContext context) {
        if(value.type() == null) return true;
        if (value.type().equals(PaymentMethodEnum.DEBITO) && (value.cuotaActual() == null || value.cuotasTotales() == null)) return true;
        if (value.type().equals(PaymentMethodEnum.CREDITO) && (value.cuotaActual() == null || value.cuotasTotales() == null)) return false;

        return value.cuotaActual() <= value.cuotasTotales();
    }
}
