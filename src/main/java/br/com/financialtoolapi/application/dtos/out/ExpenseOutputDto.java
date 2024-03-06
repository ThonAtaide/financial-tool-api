package br.com.financialtoolapi.application.dtos.out;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class ExpenseOutputDto {

    private Long id;

    private String description;

    private BigDecimal amount;

    private boolean isFixedExpense = false;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date datPurchase;

    private ExpenseCategoryDto expenseCategory;

    @Data
    public static class ExpenseCategoryDto {
        private Long id;
        private String name;
    }

}
