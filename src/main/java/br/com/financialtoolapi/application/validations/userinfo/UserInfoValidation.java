package br.com.financialtoolapi.application.validations.userinfo;

import br.com.financialtoolapi.application.dtos.in.UserRegisterInputDto;
import br.com.financialtoolapi.application.validations.ValidationRule;
import org.springframework.context.MessageSource;

public abstract class UserInfoValidation extends ValidationRule<UserRegisterInputDto> {
    public UserInfoValidation(
            final String USER_FRIENDLY_MESSAGE_CODE,
            final MessageSource messageSource
    ) {
        super(USER_FRIENDLY_MESSAGE_CODE, messageSource);
    }
}
