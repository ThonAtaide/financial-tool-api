package br.com.financialtoolapi.application.validations;

public interface ValidationRule <T> {
    void validate(T object);
}
