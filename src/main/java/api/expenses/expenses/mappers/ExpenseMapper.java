package api.expenses.expenses.mappers;

import api.expenses.expenses.entities.Credito;
import api.expenses.expenses.entities.Debito;
import api.expenses.expenses.records.ExpenseToAdd;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ExpenseMapper {
    @Mapping(target = "currency", ignore = true)
    Debito toDebito(ExpenseToAdd expenseToAdd);
    @Mapping(target = "currency", ignore = true)
    Credito toCredito(ExpenseToAdd expenseToAdd);
}
