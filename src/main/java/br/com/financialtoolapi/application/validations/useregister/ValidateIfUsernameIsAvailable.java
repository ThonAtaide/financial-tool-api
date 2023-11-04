package br.com.financialtoolapi.application.validations.useregister;

import br.com.financialtoolapi.application.domain.usecases.security.FindUserCredentialDataByUsernameUseCase;
import br.com.financialtoolapi.application.dtos.in.UserRegisterInputDto;
import br.com.financialtoolapi.application.exceptions.ResourceCreationException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ValidateIfUsernameIsAvailable implements UserRegisterValidation{

    private final FindUserCredentialDataByUsernameUseCase findUserCredentialDataByUsernameUseCase;

    @Override
    public void validate(@NonNull final UserRegisterInputDto userRegister) {
        findUserCredentialDataByUsernameUseCase
                .findUserCredentialsByUsername(userRegister.username())
                .ifPresent(it -> {
                    throw new ResourceCreationException("O nome de usuário informado já está sendo utilizado.");
                });
    }
}
