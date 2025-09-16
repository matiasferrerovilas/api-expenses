package api.expenses.expenses.entities;

import api.expenses.expenses.enums.PaymentMethodEnum;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Entity
@DiscriminatorValue("DEBITO")
@Data
@SuperBuilder
@NoArgsConstructor
public class Debito extends Gasto {

    @Override
    public PaymentMethodEnum getType() {
        return PaymentMethodEnum.DEBITO;
    }
}
