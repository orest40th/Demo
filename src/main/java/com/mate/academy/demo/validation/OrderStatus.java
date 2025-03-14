package com.mate.academy.demo.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = EnumValidator.class)
public @interface OrderStatus {
    String message() default "must be a status enum constant {COMPLETED, DELIVERED, PENDING}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
