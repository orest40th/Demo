package com.mate.academy.demo.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class DescriptionValidator implements ConstraintValidator<Description, String> {
    private static final Pattern DESCRIPTION_PATTERN = Pattern.compile(
            "^[A-Za-z0-9\\s.,!?'\"()-]+$");

    @Override
    public boolean isValid(String desc, ConstraintValidatorContext context) {
        return desc != null && DESCRIPTION_PATTERN.matcher(desc).matches();
    }
}
