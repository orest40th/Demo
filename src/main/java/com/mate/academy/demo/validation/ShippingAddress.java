package com.mate.academy.demo.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = ShippingAddressValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ShippingAddress {
    String message() default "must be 5-100 characters"
            + ", allowing letters, numbers, spaces, commas, periods, and hyphens.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
