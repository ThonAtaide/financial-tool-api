package br.com.financialtoolapi.controller.v1.response;

import java.math.BigDecimal;

public record ExpenseGroupResponseV1(String label, BigDecimal amount) {
}
