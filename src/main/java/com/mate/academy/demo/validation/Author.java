package com.mate.academy.demo.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = AuthorValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface Author {
    String message() default "first name starts with a capital letter,"
            + " followed by lowercase letters."
            + "Allows initials, hyphenated or apostrophe-containing last names (optional).";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
