package br.com.financialtoolapi.application.validations;

import br.com.financialtoolapi.application.utils.InternationalizationUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;

@RequiredArgsConstructor
public abstract class ValidationRule<T> {

    private final String USER_FRIENDLY_MESSAGE_CODE;
    private final MessageSource messageSource;

    public abstract void validate(T object);

    protected String getUserFriendlyAdviseMessage(final Object... args) {
        return InternationalizationUtils
                .getMessage(
                        messageSource,
                        USER_FRIENDLY_MESSAGE_CODE,
                        args
                );
    }

}
