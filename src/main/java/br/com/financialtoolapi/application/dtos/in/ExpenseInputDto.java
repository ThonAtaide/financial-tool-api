package br.com.financialtoolapi.application.dtos.in;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class ExpenseInputDto {

    private String description;

    private BigDecimal amount;

    private boolean isFixedExpense = false;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date datPurchase;

    private Long expenseCategory;
}
