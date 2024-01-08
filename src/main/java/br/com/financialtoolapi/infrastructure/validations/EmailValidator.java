package br.com.financialtoolapi.infrastructure.validations;

import br.com.financialtoolapi.infrastructure.validations.annotations.Email;
import io.micrometer.common.util.StringUtils;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailValidator implements ConstraintValidator<Email, String> {

    public static final String REGEX = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\\\.[A-Za-z0-9-]+)*(\\\\.[A-Za-z]{2,})$";

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (StringUtils.isBlank(value)) return false;
        final Pattern pattern =
                Pattern.compile(REGEX);
        final Matcher matcher = pattern.matcher(value);
        return matcher.matches();
    }
}
