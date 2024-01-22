package br.com.financialtoolapi.application.validations.userinfo;

import br.com.financialtoolapi.application.dtos.in.UserRegisterInputDto;
import br.com.financialtoolapi.application.exceptions.ValidationDataException;
import br.com.financialtoolapi.application.usecases.security.FindUserAccountByEmailUseCase;
import br.com.financialtoolapi.infrastructure.config.properties.UserRegisterAllowedEmailsProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ValidateIfEmailIsAllowedByWhiteLists extends AbstractUserInfoValidation {

    public static final String DETAILED_ERROR_MESSAGE = "The platform is not released yet and this e-mail is not allowed.";
    private final UserRegisterAllowedEmailsProperties userRegisterAllowedEmailsProperties;

    public ValidateIfEmailIsAllowedByWhiteLists(
            final MessageSource messageSource,
            UserRegisterAllowedEmailsProperties userRegisterAllowedEmailsProperties
    ) {
        super("sign-up.email.not-allowed", messageSource);
        this.userRegisterAllowedEmailsProperties = userRegisterAllowedEmailsProperties;
    }

    @Override
    public void validate(final UserRegisterInputDto userRegister) {
        final String userEmail = userRegister.email();
        if (isEmailNotAllowedToBeUsed(userEmail)) {
            log.debug("The user e-mail white list is enable and this E-mail {} is not allowed.", userEmail);
            throw new ValidationDataException(
                    getUserFriendlyAdviseMessage(),
                    DETAILED_ERROR_MESSAGE
            );
        }

    }

    private boolean isEmailNotAllowedToBeUsed(final String email) {
        return userRegisterAllowedEmailsProperties.isEnable() &&
                !userRegisterAllowedEmailsProperties.getEmailsList().contains(email);
    }

}
