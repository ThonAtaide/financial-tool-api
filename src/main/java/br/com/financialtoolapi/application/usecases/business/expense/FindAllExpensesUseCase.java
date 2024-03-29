package br.com.financialtoolapi.application.usecases.business.expense;

import br.com.financialtoolapi.application.domain.entities.ExpenseEntity;
import br.com.financialtoolapi.application.domain.repositories.ExpenseRepository;
import br.com.financialtoolapi.application.exceptions.InvalidQueryParamFormatException;
import br.com.financialtoolapi.application.utils.DateUtils;
import br.com.financialtoolapi.application.utils.InternationalizationUtils;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.ap.shaded.freemarker.template.utility.NumberUtil;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.NumberUtils;

import java.text.ParseException;
import java.util.*;
import java.util.regex.Pattern;

import static org.springframework.data.domain.Sort.Direction.DESC;
import static org.springframework.jdbc.support.JdbcUtils.isNumeric;

@Slf4j
@Service
@RequiredArgsConstructor
public class FindAllExpensesUseCase {

    public static final String QUERY_PARAM_PURCHASE_DATE_FROM = "from";
    public static final String QUERY_PARAM_PURCHASE_DATE_UNTIL = "until";
    public static final String EXPENSE_FIELD_DAT_PURCHASE = "datPurchase";
    public static final String QUERY_PARAM_DATE_HAS_INVALID_FORMAT_MESSAGE_CODE = "query-param-date-has-invalid-format";
    private final ExpenseRepository expenseRepository;
    private final MessageSource messageSource;

    public Page<ExpenseEntity> findAllExpensesBy(
            final int page,
            final int pageSize,
            final Map<String, String> queryParams,
            final UUID userAccountIdentifier
    ) {
        final Date from = getFromDateOrDefaultValue(queryParams);
        final Date until = getUntilDateOrDefaultValue(queryParams);
        final List<Long> categories = getCategories(queryParams);
        final String description = getDescription(queryParams);

        return expenseRepository.findAll(
                ExpenseRepository.buildPurchaseDateSpecification(from, until)
                        .and(ExpenseRepository.buildOwnerAccountSpecification(userAccountIdentifier))
                        .and(ExpenseRepository.buildCategorySpecification(categories))
                        .and(ExpenseRepository.buildExpenseDescriptionSpecification(description)),
                PageRequest.of(page, pageSize, DESC, EXPENSE_FIELD_DAT_PURCHASE)
        );
    }

    private String getDescription(Map<String, String> queryParams) {
        return queryParams.getOrDefault("description", "");
    }

    private List<Long> getCategories(final Map<String, String> queryParams) {
        final String mapValue = queryParams.getOrDefault("categories", "");
        final Pattern pattern = Pattern.compile("^[1-9][0-9]*$");
        return Arrays
                .stream(mapValue.split(","))
                .filter(it -> pattern.matcher(it).matches())
                .map(Long::parseLong).toList();
    }

    private Date convertStringParamToDate(
            final String stringDate,
            final String queryParamName,
            final Date defaultReturn
    ) {
        if (StringUtils.isBlank(stringDate)) return defaultReturn;
        try {
            return DateUtils.convertStringToDate(stringDate);
        } catch (ParseException e) {
            log.debug("Parse query param {} with value {} has failed.", queryParamName, stringDate);
            throw new InvalidQueryParamFormatException(
                    InternationalizationUtils.getMessage(messageSource, QUERY_PARAM_DATE_HAS_INVALID_FORMAT_MESSAGE_CODE, queryParamName),
                    String.format("Param %s is not according expected pattern yyyy-MM-dd.", queryParamName)
            );
        }
    }

    private Date getUntilDateOrDefaultValue(final Map<String, String> queryParams) {
        final String stringDate = queryParams.get(QUERY_PARAM_PURCHASE_DATE_UNTIL);
        return convertStringParamToDate(stringDate, QUERY_PARAM_PURCHASE_DATE_UNTIL, DateUtils.getFirstDayOfMonthDate(getFromDateOrDefaultValue(queryParams)));
    }

    private Date getFromDateOrDefaultValue(final Map<String, String> queryParams) {
        final String stringDate = queryParams.get(QUERY_PARAM_PURCHASE_DATE_FROM);
        return convertStringParamToDate(stringDate, QUERY_PARAM_PURCHASE_DATE_FROM, new Date());
    }


}
