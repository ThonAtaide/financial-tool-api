package br.com.financialtoolapi.application.utils;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.Locale;

public class InternationalizationUtils {

    public static Locale getRequestLocale() {
        return LocaleContextHolder.getLocale();
    }

    public static String getMessage(
            final MessageSource messageSource,
            final String messageCode,
            final Object... args
    ) {
        final Locale requestLocale = InternationalizationUtils.getRequestLocale();
        return messageSource.getMessage(
                messageCode,
                args,
                requestLocale
        );
    }
}
