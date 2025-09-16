package api.expenses.expenses.services.expenses;

import api.expenses.expenses.aspect.interfaces.PublishExpense;
import api.expenses.expenses.entities.Gasto;
import api.expenses.expenses.enums.BanksEnum;
import api.expenses.expenses.enums.EventType;
import api.expenses.expenses.enums.PaymentMethodEnum;
import api.expenses.expenses.records.ExpenseToAdd;
import api.expenses.expenses.repositories.CurrencyRepository;
import api.expenses.expenses.repositories.ExpenseRepository;
import api.expenses.expenses.services.expenses.typeExpense.ExpenseStrategy;
import api.expenses.expenses.services.user.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Set;

@Service
@Slf4j
@RequiredArgsConstructor
public class ExpenseIndividualService {
    private final Set<ExpenseStrategy> expenseStrategies;
    private final CurrencyRepository currencyRepository;
    private final UserService userService;
    private final ExpenseRepository expenseRepository;

    @PublishExpense(eventType = EventType.EXPENSE_ADDED, routingKey = "expense.new")
    public Gasto saveExpense(ExpenseToAdd expenseToAdd) {
        PaymentMethodEnum paymentMethodEnum = PaymentMethodEnum.valueOf(expenseToAdd.type().toUpperCase());
        var list = expenseStrategies.stream()
                .filter(strategy -> strategy.match(paymentMethodEnum))
                .toList();
        var gasto = switch (list.size()) {
            case 0 -> throw new IllegalArgumentException("Invalid payment method");
            case 1 -> list.getFirst().process(expenseToAdd);
            default -> throw new IllegalArgumentException("Multiple strategies found for payment method");
        };
        gasto.setCurrency(currencyRepository.findBySymbol(expenseToAdd.currency())
                .orElseThrow(() -> new EntityNotFoundException("Currency not found")));
        gasto.setUser(userService.getLoggedInUserEmail());

       return expenseRepository.save(gasto);
    }

    public Page<Gasto> getExpensesBy(PaymentMethodEnum paymentMethod, Long currencyId, BanksEnum bank, LocalDate date,Pageable page) {
        var user = userService.getLoggedInUserEmail();
        return expenseRepository.getExpenseBy(user, currencyId, bank, paymentMethod, page);
    }
}