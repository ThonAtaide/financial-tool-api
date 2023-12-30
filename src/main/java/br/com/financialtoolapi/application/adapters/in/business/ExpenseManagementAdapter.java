package br.com.financialtoolapi.application.adapters.in.business;

import br.com.financialtoolapi.application.domain.entities.ExpenseEntity;
import br.com.financialtoolapi.application.dtos.in.ExpenseInputDto;
import br.com.financialtoolapi.application.dtos.out.ExpenseGroupOutputDto;
import br.com.financialtoolapi.application.dtos.out.ExpenseOutputDto;
import br.com.financialtoolapi.application.exceptions.ForbiddenAccessException;
import br.com.financialtoolapi.application.mapper.ExpenseMapper;
import br.com.financialtoolapi.application.ports.in.business.ExpenseManagementPort;
import br.com.financialtoolapi.application.usecases.business.expense.*;
import br.com.financialtoolapi.application.utils.InternationalizationUtils;
import io.vavr.control.Option;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExpenseManagementAdapter implements ExpenseManagementPort {

    public static final String USER_ACCESS_NOT_AUTHORIZED_MESSAGE_CODE = "user-access-not-authorized";
    private final ExpenseMapper expenseMapper = Mappers.getMapper(ExpenseMapper.class);
    private final CreateOrUpdateExpenseUseCase createOrUpdateExpenseUseCase;
    private final FindExpenseByIdUseCase findExpenseByIdUseCase;
    private final DeleteExpenseByIdUseCase deleteExpenseByIdUseCase;
    private final FindAllExpensesUseCase findAllExpensesUseCase;
    private final GroupExpenseByCategoriesUseCase groupExpenseByCategoriesUseCase;
    private final MessageSource messageSource;

    @Override
    public ExpenseOutputDto createExpense(
            @NonNull final ExpenseInputDto expense,
            @NonNull final UUID userAccountIdentifier
    ) {
        return Option
                .of(createOrUpdateExpenseUseCase.create(expense, userAccountIdentifier))
                .map(expenseMapper::from)
                .get();
    }

    @Override
    public ExpenseOutputDto updateExpense(
            @NonNull final ExpenseInputDto expense,
            @NonNull final Long expenseId,
            @NonNull final UUID userAccountIdentifier
    ) {
        return Option
                .of(expenseId)
                .map(findExpenseByIdUseCase::findById)
                .peek(existedExpense -> validateIfUserHasAuthorization(userAccountIdentifier, existedExpense))
                .map(existedExpense -> createOrUpdateExpenseUseCase.update(expense, existedExpense))
                .map(expenseMapper::from)
                .get();
    }

    @Override
    public ExpenseOutputDto findExpenseById(final Long expenseId, final UUID userAccountIdentifier) {
        return Option.of(expenseId)
                .map(findExpenseByIdUseCase::findById)
                .peek(expense -> validateIfUserHasAuthorization(userAccountIdentifier, expense))
                .map(expenseMapper::from)
                .get();
    }

    @Override
    public Page<ExpenseOutputDto> findAllExpenses(
            final int page,
            final int pageSize,
            final Map<String, String> queryParams,
            final UUID userAccountIdentifier
    ) {
        return findAllExpensesUseCase
                .findAllExpensesBy(page, pageSize, queryParams, userAccountIdentifier)
                .map(expenseMapper::from);
    }

    @Override
    public void deleteExpenseById(final Long expenseId, final UUID userAccountIdentifier) {
        Option.of(expenseId)
                .map(findExpenseByIdUseCase::findById)
                .peek(expense -> validateIfUserHasAuthorization(userAccountIdentifier, expense))
                .peek(it -> deleteExpenseByIdUseCase.deleteExpenseById(expenseId));
    }

    @Override
    public Set<ExpenseGroupOutputDto> expensesGroupedByCategories(
            @NonNull final Date monthRange,
            @NonNull final UUID userAccountIdentifier
    ) {
        return groupExpenseByCategoriesUseCase.groupExpenseByCategories(monthRange, userAccountIdentifier);
    }

    private void validateIfUserHasAuthorization(
            @NonNull final UUID userAccountIdentifier,
            @NonNull final ExpenseEntity expenseEntity
    ) {
        final UUID expenseOwnerAccountIdentifier = expenseEntity.getOwner().getId();
        if (!userAccountIdentifier.equals(expenseOwnerAccountIdentifier)) {
            log.debug("Signed user {} has not grant to access requested resource {}.", userAccountIdentifier, expenseEntity.getId());
            throw new ForbiddenAccessException(
                    InternationalizationUtils.getMessage(messageSource, USER_ACCESS_NOT_AUTHORIZED_MESSAGE_CODE),
                    "Signed user has not grant to access requested resource."
            );
        }
    }


}
