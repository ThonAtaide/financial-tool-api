package br.com.financialtoolapi.controller.v1.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.NumberFormat;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class ExpenseResponseV1 {

    private Long id;

    private String description;

    private BigDecimal amount;

    private boolean isFixedExpense = false;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date datPurchase;

    private ExpenseCategoryV1 expenseCategory;

    @Data
    public static class ExpenseCategoryV1 {
        private Long id;
        private String name;
    }

}
