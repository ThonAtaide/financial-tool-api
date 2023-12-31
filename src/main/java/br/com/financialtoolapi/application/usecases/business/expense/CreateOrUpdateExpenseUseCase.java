package br.com.financialtoolapi.application.usecases.business.expense;

import br.com.financialtoolapi.application.domain.entities.ExpenseCategoryEntity;
import br.com.financialtoolapi.application.domain.entities.ExpenseEntity;
import br.com.financialtoolapi.application.domain.entities.UserAccountEntity;
import br.com.financialtoolapi.application.domain.repositories.ExpenseRepository;
import br.com.financialtoolapi.application.dtos.in.ExpenseInputDto;
import br.com.financialtoolapi.application.exceptions.ResourceNotFoundException;
import br.com.financialtoolapi.application.exceptions.ValidationDataException;
import br.com.financialtoolapi.application.mapper.ExpenseMapper;
import br.com.financialtoolapi.application.usecases.business.expenseCategory.FindExpenseCategoryByIdUseCase;
import br.com.financialtoolapi.application.usecases.business.userAccount.FindUserByAccountIdUseCase;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class CreateOrUpdateExpenseUseCase {

    private final ExpenseMapper expenseMapper = Mappers.getMapper(ExpenseMapper.class);
    private final FindExpenseCategoryByIdUseCase findExpenseCategoryByIdUseCase;
    private final FindUserByAccountIdUseCase findUserByAccountIdUseCase;
    private final ExpenseRepository expenseRepository;

    public ExpenseEntity create(@NonNull final ExpenseInputDto expense, @NonNull final UUID userAccountIdentifier) {
        final UserAccountEntity createdBy = findUserByAccountIdUseCase
                .findUserAccountById(userAccountIdentifier);
        final ExpenseCategoryEntity expenseCategory = getExpenseCategory(expense.getExpenseCategory());
        final ExpenseEntity expenseEntity = expenseMapper.from(expense, expenseCategory, createdBy);

        return expenseRepository.save(expenseEntity);
    }

    public ExpenseEntity update(
            @NonNull final ExpenseInputDto expense,
            @NonNull final ExpenseEntity existedExpense
    ) {
        existedExpense.setAmount(expense.getAmount());
        existedExpense.setFixedExpense(expense.isFixedExpense());
        existedExpense.setDescription(expense.getDescription());
        existedExpense.setDatPurchase(expense.getDatPurchase());
        existedExpense.setExpenseCategory(getExpenseCategory(expense.getExpenseCategory()));
        return expenseRepository.save(existedExpense);
    }

    private ExpenseCategoryEntity getExpenseCategory(@NonNull final Long expenseCategoryId) {
        try {
            return findExpenseCategoryByIdUseCase.findExpenseCategoryById(expenseCategoryId);
        } catch (ResourceNotFoundException ex) {
            log.debug("Expense category from id {} was not found.", expenseCategoryId);
            throw new ValidationDataException(ex.getUserFriendlyMessage(), ex.getMessage());
        }
    }
}
