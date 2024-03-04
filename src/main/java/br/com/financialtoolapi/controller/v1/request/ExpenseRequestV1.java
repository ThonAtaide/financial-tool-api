package br.com.financialtoolapi.controller.v1.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class ExpenseRequestV1 {

    @Size(message = "{expense-description-size-error}", min = 2, max = 50)
    private String description;

    @NotNull(message = "{expense-amount-null-error}")
    private BigDecimal amount;

    private boolean isFixedExpense = false;

    @NotNull(message = "{expense-datPurchase-null-error}")
    private Date datPurchase;

    @NotNull(message = "{expense-category-null-error}")
    private Long expenseCategory;
}
