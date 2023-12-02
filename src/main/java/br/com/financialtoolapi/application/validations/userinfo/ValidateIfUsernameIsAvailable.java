package br.com.financialtoolapi.application.validations.userinfo;

import br.com.financialtoolapi.application.domain.usecases.security.FindUserCredentialDataByUsernameUseCase;
import br.com.financialtoolapi.application.dtos.in.UserRegisterInputDto;
import br.com.financialtoolapi.application.exceptions.ValidationDataException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ValidateIfUsernameIsAvailable implements UserInfoValidation {

    private final FindUserCredentialDataByUsernameUseCase findUserCredentialDataByUsernameUseCase;

    @Override
    public void validate(@NonNull final UserRegisterInputDto userRegister) {
        findUserCredentialDataByUsernameUseCase
                .findUserCredentialsByUsername(userRegister.username())
                .ifPresent(it -> {
                    throw new ValidationDataException(
                            "O nome de usuário informado já está sendo utilizado.",
                            "O nome de usuário informado já está sendo utilizado e portanto o usuário na foi criado."
                    );
                });
    }
}
