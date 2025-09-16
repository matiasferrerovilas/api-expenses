package api.expenses.expenses.services.budget;

import api.expenses.expenses.entities.Category;
import api.expenses.expenses.mappers.BudgetMapper;
import api.expenses.expenses.records.BudgetToAdd;
import api.expenses.expenses.repositories.BudgetRepository;
import api.expenses.expenses.repositories.CategoryRepository;
import api.expenses.expenses.services.user.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class BudgetService {
    private final BudgetRepository budgetRepository;
    private final BudgetMapper budgetMapper;
    private final UserService userService;
    private final CategoryRepository categoryRepository;

    @Transactional
    public void createBudget(BudgetToAdd budgetToAdd) {
        var user = userService.getLoggedInUserEmail();
        var budget = budgetMapper.toEntity(budgetToAdd, categoryRepository);
        var category = categoryRepository.findByDescription(budgetToAdd.category())
                .orElseGet( ()-> categoryRepository.save(Category.builder().description(budgetToAdd.category()).build()));

        budget.setUser(user);
        budget.setCategory(category);

        budgetRepository.save(budget);
    }
}
