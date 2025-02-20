package com.mate.academy.demo.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class NameValidator implements ConstraintValidator<Name, String> {
    private static final Pattern FIRST_NAME_PATTERN = Pattern.compile("^[A-Za-z]+$");

    @Override
    public boolean isValid(String fName, ConstraintValidatorContext constraintValidatorContext) {
        return fName != null && FIRST_NAME_PATTERN.matcher(fName).matches();
    }
}
