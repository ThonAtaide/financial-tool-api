package br.com.financialtoolapi.application.mapper;

import br.com.financialtoolapi.application.domain.entities.ExpenseCategoryEntity;
import br.com.financialtoolapi.application.domain.entities.ExpenseEntity;
import br.com.financialtoolapi.application.domain.entities.UserAccountEntity;
import br.com.financialtoolapi.application.dtos.in.ExpenseInputDto;
import br.com.financialtoolapi.application.dtos.out.ExpenseOutputDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface ExpenseMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "expense.fixedExpense", target = "isFixedExpense")
    @Mapping(source = "expenseCategory", target = "expenseCategory")
    @Mapping(source = "userAccount", target = "owner")
    ExpenseEntity from(
            ExpenseInputDto expense,
            ExpenseCategoryEntity expenseCategory,
            UserAccountEntity userAccount
    );

    ExpenseOutputDto from(
            ExpenseEntity expenseEntity
    );
}
