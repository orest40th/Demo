package com.mate.academy.demo.service;

import com.mate.academy.demo.dto.OrderDto;
import com.mate.academy.demo.model.Order;

public interface OrderService {
    OrderDto placeOrder(Long userID);
}
