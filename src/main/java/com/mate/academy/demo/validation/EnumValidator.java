package com.mate.academy.demo.validation;

import com.mate.academy.demo.model.Order;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class EnumValidator implements ConstraintValidator<OrderStatus, Enum<?>> {
    @Override
    public boolean isValid(Enum<?> anEnum, ConstraintValidatorContext constraintValidatorContext) {
        if (anEnum == null) {
            return false;
        }

        try {
            Enum.valueOf(Order.Status.class, anEnum.name());
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
