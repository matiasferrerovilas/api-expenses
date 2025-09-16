package api.expenses.expenses.services.expenses;

import api.expenses.expenses.enums.BanksEnum;
import api.expenses.expenses.exceptions.BusinessException;
import api.expenses.expenses.helpers.PdfReaderService;
import api.expenses.expenses.services.expenses.expensesFiles.ExpenseFileStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Set;

@Service
@Slf4j
@RequiredArgsConstructor
public class ExpenseFileService {
    private final Set<ExpenseFileStrategy> expenseFileStrategies;

    private final PdfReaderService pdfReaderService;
    public void saveExpenseByFile(MultipartFile file, String bank) {
        try {
            File pdfFile = File.createTempFile("expense-", ".pdf");
            file.transferTo(pdfFile);
            String text = pdfReaderService.extractTextFromPdf(pdfFile);
            pdfFile.delete();

            BanksEnum banksEnum = BanksEnum.valueOf(bank.toUpperCase());
            var list = expenseFileStrategies.stream()
                    .filter(strategy -> strategy.match(banksEnum))
                    .toList();

            switch (list.size()) {
                case 0 -> throw new IllegalArgumentException("Invalid bank method");
                case 1 -> list.getFirst().process(text);
                default -> throw new IllegalArgumentException("Multiple strategies found for bank method");
            }
        } catch (IOException e) {
            throw new BusinessException("No se pudo procesar");
        }
    }
}