package com.mate.academy.demo.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

public class ShippingAddressValidator implements ConstraintValidator<ShippingAddress, String> {
    @Override
    public boolean isValid(String address, ConstraintValidatorContext constraintValidatorContext) {
        Pattern SHIPPING_ADDRESS_PATTERN = Pattern.compile("^[a-zA-Z0-9,.\\-\\s]$");

        return address != null && SHIPPING_ADDRESS_PATTERN.matcher(address).matches();
    }
}
