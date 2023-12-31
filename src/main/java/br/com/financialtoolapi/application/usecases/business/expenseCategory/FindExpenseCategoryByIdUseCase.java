package br.com.financialtoolapi.application.usecases.business.expenseCategory;

import br.com.financialtoolapi.application.domain.entities.ExpenseCategoryEntity;
import br.com.financialtoolapi.application.domain.repositories.ExpenseCategoryRepository;
import br.com.financialtoolapi.application.exceptions.ResourceNotFoundException;
import br.com.financialtoolapi.application.utils.InternationalizationUtils;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FindExpenseCategoryByIdUseCase {

    public static final String EXPENSE_CATEGORY_NOT_FOUND_MESSAGE_CODE = "expense-category-not-found";
    private final ExpenseCategoryRepository expenseCategoryRepository;
    private final MessageSource messageSource;

    public ExpenseCategoryEntity findExpenseCategoryById(final @NonNull Long id) {
        return expenseCategoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                                InternationalizationUtils.getMessage(messageSource, EXPENSE_CATEGORY_NOT_FOUND_MESSAGE_CODE),
                                String.format("Do not exists a expense category for id %s.", id)
                        )
                );
    }
}
