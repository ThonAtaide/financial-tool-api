package br.com.financialtoolapi.application.usecases.business.expense;

import br.com.financialtoolapi.application.domain.repositories.ExpenseRepository;
import br.com.financialtoolapi.application.dtos.out.ExpenseGroupOutputDto;
import br.com.financialtoolapi.application.utils.DateUtils;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GroupExpenseByCategoriesUseCase {

    private final ExpenseRepository expenseRepository;

    public Set<ExpenseGroupOutputDto> groupExpenseByCategories(
            @NonNull final Date date,
            @NonNull final UUID userAccountIdentifier
    ) {
        return expenseRepository
                .groupByExpenseCategories(userAccountIdentifier, DateUtils.getLastDayOfMonthDate(date), DateUtils.getFirstDayOfMonthDate(date));

    }

}
