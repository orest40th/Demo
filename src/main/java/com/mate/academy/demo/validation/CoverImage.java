package com.mate.academy.demo.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = CoverImageValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface CoverImage {
    String message() default "must be a slash-separated path ending with an extension"
            + " jpg, jpeg, png, bmp";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
