package br.com.financialtoolapi.controller.v1;

import br.com.financialtoolapi.application.ports.in.business.ExpenseManagementPort;
import br.com.financialtoolapi.controller.v1.mapper.ExpenseMapper;
import br.com.financialtoolapi.controller.v1.request.ExpenseRequestV1;
import br.com.financialtoolapi.controller.v1.response.ExpenseResponseV1;
import io.vavr.control.Option;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

import static br.com.financialtoolapi.infrastructure.config.security.filters.HeaderAppenderFilter.X_USER_IDENTIFIER_HEADER;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/expenses", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
public class ExpenseController {

    private final ExpenseMapper expenseMapper = Mappers.getMapper(ExpenseMapper.class);
    private final ExpenseManagementPort expenseService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ExpenseResponseV1 createExpense(
            @RequestHeader(name = X_USER_IDENTIFIER_HEADER) final UUID userAccountIdentifier,
            @Valid @RequestBody final ExpenseRequestV1 expenseRequest
    ) {
        return Option
                .of(expenseRequest)
                .peek(it -> log.debug("User: {} is registering new expense {}", userAccountIdentifier, expenseRequest))
                .map(expenseMapper::from)
                .map(it -> expenseService.createExpense(it, userAccountIdentifier))
                .map(expenseMapper::from)
                .get();
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/{expense-id}")
    public ExpenseResponseV1 updateExpense(
            @RequestHeader(name = X_USER_IDENTIFIER_HEADER) final UUID userAccountIdentifier,
            @Valid @RequestBody final ExpenseRequestV1 expenseRequest,
            @PathVariable("expense-id") final Long expenseId
    ) {
        return Option
                .of(expenseRequest)
                .peek(it -> log.debug("User: {} is updating expense from id {} - {}", userAccountIdentifier, expenseId, expenseRequest))
                .map(expenseMapper::from)
                .map(it -> expenseService.updateExpense(it, expenseId, userAccountIdentifier))
                .map(expenseMapper::from)
                .get();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{expense-id}")
    public ExpenseResponseV1 findExpenseById(
            @RequestHeader(name = X_USER_IDENTIFIER_HEADER) final UUID userAccountIdentifier,
            @PathVariable("expense-id") final Long expenseId
    ) {
        return Option
                .of(expenseService.findExpenseById(expenseId, userAccountIdentifier))
                .map(expenseMapper::from)
                .get();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public Page<ExpenseResponseV1> findAllExpenses(
            @RequestHeader(name = X_USER_IDENTIFIER_HEADER, required = false) final UUID userAccountIdentifier,
            @RequestHeader(required = false, defaultValue = "0") int page,
            @RequestHeader(required = false, defaultValue = "20") int pageSize,
            @RequestParam Map<String, String> queryParams
    ) {
        return Option.of(PageRequest.of(page, pageSize))
                .map(pageable -> expenseService
                        .findAllExpenses(page, pageSize, queryParams, userAccountIdentifier)
                        .map(expenseMapper::from)
                ).get();
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{expense-id}")
    public void deleteExpenseById(
            @RequestHeader(name = X_USER_IDENTIFIER_HEADER) final UUID userAccountIdentifier,
            @PathVariable("expense-id") final Long expenseId
    ) {
        log.debug("User: {} is deleting expense from id {}", userAccountIdentifier, expenseId);
        expenseService.deleteExpenseById(expenseId, userAccountIdentifier);
    }

}
