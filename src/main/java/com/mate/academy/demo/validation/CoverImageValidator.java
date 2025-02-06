package com.mate.academy.demo.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class CoverImageValidator implements ConstraintValidator<CoverImage, String> {
    private static final Pattern COVER_IMAGE_PATTERN = Pattern.compile(
            "^([a-zA-Z0-9-_+/]+/)*[a-zA-Z0-9-_+/]+\\.(jpg|jpeg|png|gif|bmp|tiff)$");

    @Override
    public boolean isValid(String coverImage, ConstraintValidatorContext context) {
        return coverImage != null && COVER_IMAGE_PATTERN.matcher(coverImage).matches();
    }
}
