package br.com.financialtoolapi.factory;

import br.com.financialtoolapi.application.domain.entities.ExpenseCategoryEntity;
import lombok.experimental.UtilityClass;

import java.util.UUID;

@UtilityClass
public class ExpenseCategoryFactory {

    public ExpenseCategoryEntity buildWith() {
        return buildWith(UUID.randomUUID().toString());
    }

    public ExpenseCategoryEntity buildWith(final String name
    ) {
        return ExpenseCategoryEntity.builder().name(name).build();
    }
}
