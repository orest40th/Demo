package com.mate.academy.demo.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class AuthorValidator implements ConstraintValidator<Author, String> {
    private static final Pattern AUTHOR_PATTERN = Pattern.compile(
            "^(?:[A-Z][a-z]+(?:[-'][A-Za-z]+)?|(?:[A-Z]"
                    + "\\.(?:\\s[A-Z]\\.)?)) (?:[A-Z][a-z]+(?:[-'][A-Za-z]+)?)$"
    );

    @Override
    public boolean isValid(String author, ConstraintValidatorContext context) {
        return author != null && AUTHOR_PATTERN.matcher(author).matches();
    }
}
