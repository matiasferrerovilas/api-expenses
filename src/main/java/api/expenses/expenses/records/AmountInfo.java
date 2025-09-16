package api.expenses.expenses.records;

import java.math.BigDecimal;

public record AmountInfo(BigDecimal pesos, BigDecimal dolares, boolean hasForeignCurrency) {}

