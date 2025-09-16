package api.expenses.expenses.controller;

import api.expenses.expenses.records.BalanceRecord;
import api.expenses.expenses.services.balance.CalculateBalanceService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/balance")
@Tag(name = "Balance", description = "Manejo de balance del usuario.")
public class BalanceController {
    private final CalculateBalanceService calculateBalanceService;

    @GetMapping()
    public Set<BalanceRecord> getBalance(@RequestParam(required = false) Integer year, @RequestParam(required = false) Integer month,
                                         @RequestParam(required = false) Integer currencyId) {
        return calculateBalanceService.getBalance(year, month, currencyId);
    }
}
