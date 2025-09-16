package api.expenses.expenses.records;

import java.math.BigDecimal;
import java.time.LocalDate;

public record BalanceRecord(Integer year, Integer month, BigDecimal balance, String symbol) {
}
