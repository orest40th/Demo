package com.mate.academy.demo.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = DescriptionValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface Description {
    String message() default "must contain only valid characters"
            + " (alphanumeric characters, spaces, and basic punctuation )";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
