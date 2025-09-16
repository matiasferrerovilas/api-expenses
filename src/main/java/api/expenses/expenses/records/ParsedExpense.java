package api.expenses.expenses.records;

import api.expenses.expenses.entities.Currency;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ParsedExpense(
        LocalDateTime dateTime,
        String reference,
        String installment,
        String comprobante,
        Currency currency,
        BigDecimal amountPesos,
        BigDecimal amountDolares
) {}
