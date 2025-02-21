package com.mate.academy.demo.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.lang.reflect.Field;
import java.util.Objects;

public class PasswordValidator implements ConstraintValidator<FieldMatch, Object> {
    private String password;
    private String repeatPassword;

    @Override
    public void initialize(FieldMatch constraintAnnotation) {
        this.password = constraintAnnotation.password();
        this.repeatPassword = constraintAnnotation.repeatPassword();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        try {
            Field passwordField = value.getClass().getDeclaredField(password);
            Field repeatPasswordField = value.getClass().getDeclaredField(repeatPassword);
            passwordField.setAccessible(true);
            repeatPasswordField.setAccessible(true);

            Object firstValue = passwordField.get(value);
            Object secondValue = repeatPasswordField.get(value);

            return Objects.equals(firstValue, secondValue);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            return false;
        }
    }
}
