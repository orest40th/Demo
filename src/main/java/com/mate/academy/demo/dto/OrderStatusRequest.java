package com.mate.academy.demo.dto;

import com.mate.academy.demo.model.Order;
import jakarta.validation.constraints.NotBlank;

public record OrderStatusRequest(@NotBlank Order.Status status) {
}
