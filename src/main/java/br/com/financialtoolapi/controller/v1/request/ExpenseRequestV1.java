package br.com.financialtoolapi.controller.v1.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class ExpenseRequestV1 {

    private String description;

    private BigDecimal amount;

    private boolean isFixedExpense = false;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date datPurchase;

    private Long expenseCategory;
}
