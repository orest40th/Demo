package com.mate.academy.demo.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class TitleValidator implements ConstraintValidator<Title, String> {
    private static final Pattern TITLE_PATTERN = Pattern.compile(
            "^[A-Za-z0-9 ,.:'\"!?()-]{2,100}$");

    @Override
    public boolean isValid(String title, ConstraintValidatorContext constraintValidatorContext) {
        return title != null && TITLE_PATTERN.matcher(title).matches();
    }
}
