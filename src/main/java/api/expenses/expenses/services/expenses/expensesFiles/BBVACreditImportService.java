package api.expenses.expenses.services.expenses.expensesFiles;

import api.expenses.expenses.enums.BanksEnum;
import api.expenses.expenses.enums.CurrencyEnum;
import api.expenses.expenses.enums.PaymentMethodEnum;
import api.expenses.expenses.helpers.PdfExtractprHelper;
import api.expenses.expenses.records.ParsedExpense;
import api.expenses.expenses.services.expenses.ExpenseIndividualService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@Slf4j
public class BBVACreditImportService extends ExpenseFileStrategy {

    public BBVACreditImportService(ExpenseIndividualService expenseIndividualService, List<PdfExtractprHelper> parsers) {
        super(expenseIndividualService, parsers);
    }

    @Override
    public boolean match(BanksEnum banksEnum) {
        return BanksEnum.BBVA.equals(banksEnum);
    }
    @Override
    public PaymentMethodEnum getBankMethod() {
        return PaymentMethodEnum.CREDITO;
    }
    @Override
    public BanksEnum getBank() {
        return BanksEnum.BBVA;
    }

    @Override
    protected BigDecimal resolveAmount(ParsedExpense e) {
        return CurrencyEnum.USD.equals(CurrencyEnum.valueOf(e.currency().getSymbol()))
                ? e.amountDolares()
                : e.amountPesos();
    }
}
