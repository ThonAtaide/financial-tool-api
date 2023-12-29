package br.com.financialtoolapi.application.usecases.business.expense;

import br.com.financialtoolapi.application.domain.entities.ExpenseEntity;
import br.com.financialtoolapi.application.domain.repositories.ExpenseRepository;
import br.com.financialtoolapi.application.utils.DateUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

import static org.springframework.data.domain.Sort.Direction.ASC;

@Service
@RequiredArgsConstructor
public class FindAllExpensesUseCase {

    public static final String QUERY_PARAM_PURCHASE_DATE_FROM = "from";
    public static final String QUERY_PARAM_PURCHASE_DATE_UNTIL = "until";
    public static final String EXPENSE_FIELD_DAT_PURCHASE = "datPurchase";
    private final ExpenseRepository expenseRepository;

    public Page<ExpenseEntity> findAllExpensesBy(
            final int page,
            final int pageSize,
            final Map<String, String> queryParams,
            final UUID userAccountIdentifier
    ) {
        final Date from = getFromDateOrDefaultValue(queryParams);
        final Date until = getUntilDateOrDefaultValue(queryParams);

        return expenseRepository.findAll(
                ExpenseRepository.buildPurchaseDateSpecification(from, until)
                        .and(ExpenseRepository.buildOwnerAccountSpecification(userAccountIdentifier)),
                PageRequest.of(page, pageSize, ASC, EXPENSE_FIELD_DAT_PURCHASE)
        );
    }

    private Date getUntilDateOrDefaultValue(final Map<String, String> queryParams) {
        try {
            return DateUtils.convertStringToDate(queryParams.get(QUERY_PARAM_PURCHASE_DATE_UNTIL));
        } catch (final Exception ex) {
            final var localDateNow = LocalDate.now();
            return Date
                    .from(
                            LocalDate.of(
                                            localDateNow.getYear(),
                                            localDateNow.getMonth(),
                                            1
                                    ).atStartOfDay(ZoneId.systemDefault())
                                    .toInstant()
                    );
        }
    }

    private Date getFromDateOrDefaultValue(final Map<String, String> queryParams) {
        try {
            return DateUtils.convertStringToDate(queryParams.get(QUERY_PARAM_PURCHASE_DATE_FROM));
        } catch (final Exception ex) {
            return new Date();
        }
    }
}
