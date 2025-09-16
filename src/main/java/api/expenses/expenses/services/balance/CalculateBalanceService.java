package api.expenses.expenses.services.balance;

import api.expenses.expenses.records.BalanceRecord;
import api.expenses.expenses.repositories.ExpenseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@Slf4j
@RequiredArgsConstructor
public class CalculateBalanceService {
    private final ExpenseRepository expenseRepository;

    public Set<BalanceRecord> getBalance(Integer year, Integer month, Integer currencyId) {
        return expenseRepository.getBalanceByYearAndMonth(year, month, currencyId);
    }
}
