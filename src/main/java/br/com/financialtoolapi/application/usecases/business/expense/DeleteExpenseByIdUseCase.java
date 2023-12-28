package br.com.financialtoolapi.application.usecases.business.expense;

import br.com.financialtoolapi.application.domain.repositories.ExpenseRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeleteExpenseByIdUseCase {

    private final ExpenseRepository expenseRepository;

    public void deleteExpenseById(@NonNull final Long expenseId) {
        expenseRepository.deleteById(expenseId);
    }
}
