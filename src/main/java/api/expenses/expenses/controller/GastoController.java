package api.expenses.expenses.controller;

import api.expenses.expenses.entities.Gasto;
import api.expenses.expenses.enums.BanksEnum;
import api.expenses.expenses.enums.PaymentMethodEnum;
import api.expenses.expenses.records.ExpenseToAdd;
import api.expenses.expenses.services.expenses.ExpenseFileService;
import api.expenses.expenses.services.expenses.ExpenseIndividualService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/expenses")
@Tag(name = "Expenses", description = "API para la gestión de gastos personales")
public class GastoController {
    private final ExpenseIndividualService expenseIndividualService;
    private final ExpenseFileService expenseFileService;

    @Operation(
        summary = "Obtener gastos",
        description = "Recupera una lista de gastos filtrados por diferentes criterios",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Lista de gastos encontrados",
                content = @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = Gasto.class))
                )
            )
        }
    )
    @GetMapping
    public Page<Gasto> getExpensesBy(
            @Parameter(description = "Método de pago para filtrar") 
            @RequestParam(required = false) PaymentMethodEnum paymentMethod,

            @Parameter(description = "ID de la moneda para filtrar") 
            @RequestParam(required = false) Long currencyId,
            
            @Parameter(description = "Banco asociado al gasto") 
            @RequestParam(required = false) BanksEnum bank,
            @Parameter(description = "Fecha asociada al gasto")
            @RequestParam(required = false) LocalDate date,
            Pageable page) {
        return expenseIndividualService.getExpensesBy(paymentMethod, currencyId, bank, date, page);
    }

    @Operation(
        summary = "Crear un nuevo gasto",
        description = "Crea un nuevo registro de gasto en el sistema",
        responses = {
            @ApiResponse(
                responseCode = "201",
                description = "Gasto creado exitosamente"
            ),
            @ApiResponse(
                responseCode = "400",
                description = "Datos de entrada inválidos"
            )
        }
    )
    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public Gasto saveExpense(
            @Parameter(description = "Datos del gasto a crear", required = true)
            @Valid @RequestBody ExpenseToAdd expenseToAdd) {
        return expenseIndividualService.saveExpense(expenseToAdd);
    }

    @Operation(
        summary = "Importar gastos desde archivo",
        description = "Importa múltiples gastos desde un archivo (formato específico por banco)",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Archivo procesado exitosamente"
            ),
            @ApiResponse(
                responseCode = "400",
                description = "Error en el formato del archivo o datos inválidos"
            )
        }
    )
    @PostMapping(value = "/import-file", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void saveExpenseByFile(
            @Parameter(description = "Archivo con los gastos a importar", required = true)
            @RequestParam("file") MultipartFile file, 
            
            @Parameter(description = "Nombre del banco para el formato del archivo", required = true)
            @RequestParam("bank") String bank) {
        expenseFileService.saveExpenseByFile(file, bank);
    }
}
