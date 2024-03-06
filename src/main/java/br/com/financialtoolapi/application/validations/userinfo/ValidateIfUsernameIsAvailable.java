package br.com.financialtoolapi.application.validations.userinfo;

import br.com.financialtoolapi.application.dtos.in.UserRegisterInputDto;
import br.com.financialtoolapi.application.exceptions.ValidationDataException;
import br.com.financialtoolapi.application.usecases.security.FindUserCredentialDataByUsernameUseCase;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ValidateIfUsernameIsAvailable extends AbstractUserInfoValidation {

    public static final String DETAILED_ERROR_MESSAGE = "The username %s has already be taken.";
    private final FindUserCredentialDataByUsernameUseCase findUserCredentialDataByUsernameUseCase;

    public ValidateIfUsernameIsAvailable(
            final MessageSource messageSource,
            final FindUserCredentialDataByUsernameUseCase findUserCredentialDataByUsernameUseCase
    ) {
        super("sign-up.username.already-exists", messageSource);
        this.findUserCredentialDataByUsernameUseCase = findUserCredentialDataByUsernameUseCase;
    }

    @Override
    public void validate(@NonNull final UserRegisterInputDto userRegister) {
        findUserCredentialDataByUsernameUseCase
                .findUserCredentialsByUsername(userRegister.username())
                .ifPresent(it -> {
                    log.debug("Username {} already being used error.", userRegister.email());
                    final String userFriendlyErrorMessage = getUserFriendlyAdviseMessage();
                    throw new ValidationDataException(
                            userFriendlyErrorMessage,
                            String.format(DETAILED_ERROR_MESSAGE, userRegister.username())
                    );
                });
    }
}
