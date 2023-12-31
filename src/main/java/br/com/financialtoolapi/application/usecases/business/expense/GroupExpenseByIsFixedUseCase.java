package br.com.financialtoolapi.application.usecases.business.expense;

import br.com.financialtoolapi.application.domain.repositories.ExpenseRepository;
import br.com.financialtoolapi.application.dtos.out.ExpenseGroupOutputDto;
import br.com.financialtoolapi.application.utils.DateUtils;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GroupExpenseByIsFixedUseCase {

    private final ExpenseRepository expenseRepository;

    public Set<ExpenseGroupOutputDto> groupExpenseByIsFixed(
            @NonNull final Date date,
            @NonNull final UUID userAccountIdentifier
    ) {
        return expenseRepository
                .groupByExpenseIsFixed(userAccountIdentifier, DateUtils.getLastDayOfMonthDate(date), DateUtils.getFirstDayOfMonthDate(date))
                .stream()
                .map(it -> {
                    final boolean isFixed = (boolean) it.get("isFixed");
                    final String label = isFixed? "Fixed" : "Not fixed";
                    final BigDecimal amount = (BigDecimal) it.get("amount");
                    return new ExpenseGroupOutputDto(label, amount);
                }).collect(Collectors.toSet());
    }

}
