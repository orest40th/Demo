package com.mate.academy.demo.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = TitleValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface Title {
    String message() default "follows an invalid format. "
            + "The right format allows letters, "
            + "numbers, spaces, commas, "
            + "periods, colons, quotes, "
            + "exclamation marks, "
            + "question marks "
            + "and parentheses.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
