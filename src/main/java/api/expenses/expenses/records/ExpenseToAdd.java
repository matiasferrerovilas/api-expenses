package api.expenses.expenses.records;

import api.expenses.expenses.constrains.ValidCuotas;
import api.expenses.expenses.enums.BanksEnum;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@ValidCuotas
public record ExpenseToAdd(
        @NotNull BigDecimal amount,
        @NotNull(message = "Fecha no puede ser nula") LocalDateTime dateTime,
        @NotNull(message = "Debe indicar una descripción") String description,
        CategoryRecord category,
        @NotNull(message = "Debe indicar un tipo de gasto") String type,
        @NotNull(message = "Debe indicar un tipo de moneda")  String currency,
        Integer cuotaActual,
        Integer cuotasTotales,
        int year,
        int month,
        BanksEnum bank
) { }