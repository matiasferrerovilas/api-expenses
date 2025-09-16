package api.expenses.expenses.controller;

import api.expenses.expenses.services.budget.BudgetService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/budget")
@Tag(name = "Presupuesto", description = "Manejo del presupuesto del usuario.")
public class BudgetController {
    private final BudgetService budgetService;
}
