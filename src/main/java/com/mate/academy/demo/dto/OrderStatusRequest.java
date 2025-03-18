package com.mate.academy.demo.dto;

import com.mate.academy.demo.model.Order;
import jakarta.validation.constraints.NotNull;

public record OrderStatusRequest(@NotNull Order.Status status) {
}
