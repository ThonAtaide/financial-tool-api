package br.com.financialtoolapi.controller.v1.mapper;

import br.com.financialtoolapi.application.dtos.in.ExpenseInputDto;
import br.com.financialtoolapi.application.dtos.out.ExpenseOutputDto;
import br.com.financialtoolapi.controller.v1.request.ExpenseRequestV1;
import br.com.financialtoolapi.controller.v1.response.ExpenseResponseV1;
import org.mapstruct.Mapper;

@Mapper
public interface ExpenseMapper {

    ExpenseInputDto from(ExpenseRequestV1 expenseRequestV1);

    ExpenseResponseV1 from(ExpenseOutputDto expenseOutputDto);
}
