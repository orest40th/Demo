package com.mate.academy.demo.dto;

import com.mate.academy.demo.model.Order;
import com.mate.academy.demo.validation.OrderStatus;

public record OrderStatusRequest(@OrderStatus Order.Status status) {
}
