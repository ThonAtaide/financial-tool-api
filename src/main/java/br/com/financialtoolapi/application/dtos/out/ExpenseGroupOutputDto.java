package br.com.financialtoolapi.application.dtos.out;

import java.math.BigDecimal;

public record ExpenseGroupOutputDto(Long identifier, String label, BigDecimal amount) {
}
