package api.expenses.expenses

import api.expenses.expenses.services.expenses.ExpenseIndividualService
import spock.lang.Specification

import java.time.LocalDateTime
import java.time.Month

class ExpenseIndividualServiceTest extends Specification {

    ExpenseIndividualService expenseService;

    def setup() {
        expenseService = new ExpenseIndividualService()
    }

    def "Dado un Usuario recupero su balance del mes" (){
        given: "Una fecha"
        LocalDateTime fixedDateTime = LocalDateTime.of(2025, Month.JUNE, 8, 20, 17, 44, 0)
        and: "Un usuario logueado correctamente"

        when:
        var expenseRecordList =  expenseService.getExpensesByYearAndMonth(fixedDateTime)
        then:
        expenseRecordList.size() == 1
        noExceptionThrown()
    }
}
