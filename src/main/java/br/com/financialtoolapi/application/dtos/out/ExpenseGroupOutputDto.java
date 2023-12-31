package br.com.financialtoolapi.application.dtos.out;

import java.math.BigDecimal;

public record ExpenseGroupOutputDto(String label, BigDecimal amount) {
}
