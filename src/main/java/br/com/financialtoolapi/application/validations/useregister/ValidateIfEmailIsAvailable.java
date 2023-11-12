package br.com.financialtoolapi.application.validations.useregister;

import br.com.financialtoolapi.application.domain.usecases.security.FindUserAccountByEmailUseCase;
import br.com.financialtoolapi.application.dtos.in.UserRegisterInputDto;
import br.com.financialtoolapi.application.exceptions.ResourceCreationException;
import br.com.financialtoolapi.application.exceptions.ValidationDataException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ValidateIfEmailIsAvailable implements UserRegisterValidation {

    private final FindUserAccountByEmailUseCase findUserAccountByEmailUseCase;

    @Override
    public void validate(UserRegisterInputDto userRegister) {
        findUserAccountByEmailUseCase
                .fetchUserAccountByEmail(userRegister.email())
                .ifPresent(it -> {
                    throw new ValidationDataException(
                            String.format("O email %s já está sendo utilizado e portanto o usuário não pôde ser criado.", userRegister.email())
                    );
                });
    }
}
