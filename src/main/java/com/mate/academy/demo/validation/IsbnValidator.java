package com.mate.academy.demo.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class IsbnValidator implements ConstraintValidator<Isbn, String> {
    private static final Pattern ISBN_PATTERN = Pattern.compile("^(?:\\d{9}[\\dX]|\\d{13})$");

    @Override
    public boolean isValid(String isbn, ConstraintValidatorContext context) {
        return isbn != null && ISBN_PATTERN.matcher(isbn).matches();
    }
}
