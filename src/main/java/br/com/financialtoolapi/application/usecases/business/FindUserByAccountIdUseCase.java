package br.com.financialtoolapi.application.usecases.business;

import br.com.financialtoolapi.application.domain.entities.UserAccountEntity;
import br.com.financialtoolapi.application.domain.repositories.UserAccountRepository;
import br.com.financialtoolapi.application.exceptions.UnexpectedInternalErrorException;
import br.com.financialtoolapi.application.utils.InternationalizationUtils;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FindUserByAccountIdUseCase {

    public static final String USER_ACCOUNT_NOT_FOUND_MESSAGE_CODE = "user-account-not-found";
    private final UserAccountRepository userAccountRepository;
    private final MessageSource messageSource;

    public UserAccountEntity findUserAccountById(@NonNull final UUID userAccountIdentifier) {
        return userAccountRepository.findById(userAccountIdentifier)
                .orElseThrow(() -> new UnexpectedInternalErrorException(
                        InternationalizationUtils.getMessage(messageSource, USER_ACCOUNT_NOT_FOUND_MESSAGE_CODE),
                        "User account was not found and the operation could not proceed."
                ));
    }
}
