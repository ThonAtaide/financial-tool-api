package br.com.financialtoolapi.infrastructure.validations.annotations;


import br.com.financialtoolapi.infrastructure.validations.EmailValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({FIELD})
@Retention(RUNTIME)
@Constraint(validatedBy = EmailValidator.class)
@Documented
public @interface Email {
    String message() default "{sign-up.email.invalid-pattern}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
