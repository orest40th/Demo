package com.mate.academy.demo.dto;

import jakarta.validation.constraints.NotBlank;

public record OrderRequest(@NotBlank String shippingAddress) {
}
