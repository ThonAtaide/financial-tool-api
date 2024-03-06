package br.com.financialtoolapi.application.usecases.business.expense;

import br.com.financialtoolapi.application.domain.entities.ExpenseEntity;
import br.com.financialtoolapi.application.domain.repositories.ExpenseRepository;
import br.com.financialtoolapi.application.exceptions.ResourceNotFoundException;
import br.com.financialtoolapi.application.utils.InternationalizationUtils;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FindExpenseByIdUseCase {

    public static final String USER_EXPENSE_NOT_FOUND_MESSAGE_CODE = "user-expense-not-found";
    private final ExpenseRepository expenseRepository;
    private final MessageSource messageSource;

    public ExpenseEntity findById(@NonNull final Long expenseId) {
        return expenseRepository
                .findById(expenseId)
                .orElseThrow(() -> new ResourceNotFoundException(
                                InternationalizationUtils.getMessage(messageSource, USER_EXPENSE_NOT_FOUND_MESSAGE_CODE),
                                "The user expense was not found."
                        )
                );
    }
}
