package br.com.financialtoolapi.application.validations.userinfo;

import br.com.financialtoolapi.application.usecases.security.FindUserAccountByEmailUseCase;
import br.com.financialtoolapi.application.dtos.in.UserRegisterInputDto;
import br.com.financialtoolapi.application.exceptions.ValidationDataException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ValidateIfEmailIsAvailable extends UserInfoValidation {

    public static final String DETAILED_ERROR_MESSAGE = "The email %s has already be taken.";
    private final FindUserAccountByEmailUseCase findUserAccountByEmailUseCase;

    public ValidateIfEmailIsAvailable(
            final MessageSource messageSource,
            final FindUserAccountByEmailUseCase findUserAccountByEmailUseCase
    ) {
        super("sign-up.email.already-exists", messageSource);
        this.findUserAccountByEmailUseCase = findUserAccountByEmailUseCase;
    }

    @Override
    public void validate(UserRegisterInputDto userRegister) {
        findUserAccountByEmailUseCase
                .fetchUserAccountByEmail(userRegister.email())
                .ifPresent(it -> {
                    log.debug("E-mail {} already being used error.", userRegister.email());
                    final String userFriendlyErrorMessage = getUserFriendlyAdviseMessage();
                    throw new ValidationDataException(
                            userFriendlyErrorMessage,
                            String.format(DETAILED_ERROR_MESSAGE, userRegister.email())
                    );
                });
    }

}
