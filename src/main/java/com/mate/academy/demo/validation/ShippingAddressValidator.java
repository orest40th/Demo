package com.mate.academy.demo.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class ShippingAddressValidator implements ConstraintValidator<ShippingAddress, String> {
    private static final Pattern SHIPPING_ADDRESS_PATTERN =
            Pattern.compile("^[a-zA-Z0-9,.\\-\\s]$");

    @Override
    public boolean isValid(String address, ConstraintValidatorContext constraintValidatorContext) {
        return address != null && SHIPPING_ADDRESS_PATTERN.matcher(address).matches();
    }
}
