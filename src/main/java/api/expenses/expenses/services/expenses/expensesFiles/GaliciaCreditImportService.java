package api.expenses.expenses.services.expenses.expensesFiles;

import api.expenses.expenses.enums.BanksEnum;
import api.expenses.expenses.enums.PaymentMethodEnum;
import api.expenses.expenses.records.ParsedExpense;
import api.expenses.expenses.helpers.PdfExtractprHelper;
import api.expenses.expenses.services.expenses.ExpenseIndividualService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@Slf4j
public class GaliciaCreditImportService extends ExpenseFileStrategy {


    public GaliciaCreditImportService(ExpenseIndividualService expenseIndividualService, List<PdfExtractprHelper> parsers) {
        super(expenseIndividualService, parsers);
    }

    @Override
    public boolean match(BanksEnum banksEnum) {
        return BanksEnum.GALICIA.equals(banksEnum);
    }

    @Override
    public BanksEnum getBank() {
        return BanksEnum.GALICIA;
    }

    @Override
    public PaymentMethodEnum getBankMethod() {
        return PaymentMethodEnum.CREDITO;
    }

    @Override
    protected BigDecimal resolveAmount(ParsedExpense e) {
        return e.amountPesos() == null ? e.amountDolares() : e.amountPesos();
    }
}