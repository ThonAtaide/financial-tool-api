package br.com.financialtoolapi.application.ports.in.business;

import br.com.financialtoolapi.application.dtos.in.ExpenseInputDto;
import br.com.financialtoolapi.application.dtos.out.ExpenseGroupOutputDto;
import br.com.financialtoolapi.application.dtos.out.ExpenseOutputDto;
import org.springframework.data.domain.Page;

import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public interface ExpenseManagementPort {

    ExpenseOutputDto createExpense(ExpenseInputDto expense, UUID userAccountIdentifier);

    ExpenseOutputDto updateExpense(ExpenseInputDto expense, Long expenseId, UUID userAccountIdentifier);

    ExpenseOutputDto findExpenseById(Long expenseId, UUID userAccountIdentifier);

    Page<ExpenseOutputDto> findAllExpenses(
            final int page,
            final int pageSize,
            Map<String, String> queryParams,
            UUID userAccountIdentifier
    );

    void deleteExpenseById(Long expenseId, UUID userAccountIdentifier);

    Set<ExpenseGroupOutputDto> expensesGroupedByCategories(Date monthRange, UUID userAccountIdentifier);

    Set<ExpenseGroupOutputDto> expensesGroupedByIsFixed(Date monthRange, UUID userAccountIdentifier);
}
