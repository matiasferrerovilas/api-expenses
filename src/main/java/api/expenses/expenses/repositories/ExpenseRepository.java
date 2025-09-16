package api.expenses.expenses.repositories;

import api.expenses.expenses.entities.Gasto;
import api.expenses.expenses.enums.BanksEnum;
import api.expenses.expenses.enums.PaymentMethodEnum;
import api.expenses.expenses.records.BalanceRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Set;

@Repository
public interface ExpenseRepository extends JpaRepository<Gasto, Long> {
    @Query(value = """
            SELECT g.year,g.month, SUM(g.amount) as total, c.symbol
            FROM gastos g
            INNER JOIN  currency c ON g.currency_id = c.id
            WHERE (:year IS NULL OR g.year = :year)
                  AND (:month IS NULL OR g.month = :month)
                  AND (:currencyId IS NULL OR g.currency_id = :currencyId)
            GROUP BY g.year, g.month, g.currency_id
            ORDER BY 1,2;
                    """, nativeQuery = true)
    Set<BalanceRecord> getBalanceByYearAndMonth(Integer year, Integer month, Integer currencyId);

    @Query(value = """
            SELECT g
            FROM Gasto g
            JOIN FETCH g.currency c
            WHERE (:bank IS NULL OR g.bank = :bank)
                    AND (:bank IS NULL OR g.bank = :bank)
                    AND (:currencyId IS NULL OR g.currency.id = :currencyId)
                    AND (:user IS NULL OR g.user = :user)
                    AND (:date IS NULL OR g.year = YEAR(:date) AND g.month = MONTH(:date))
                    AND (:paymentMethod IS NULL OR g.type = :paymentMethod)
            ORDER BY g.dateTime DESC
            """)
    Page<Gasto> getExpenseBy(String user, Long currencyId, BanksEnum bank, PaymentMethodEnum paymentMethod, LocalDate date,Pageable page);
}
