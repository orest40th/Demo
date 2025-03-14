package com.mate.academy.demo.service;

import com.mate.academy.demo.dto.OrderDto;
import com.mate.academy.demo.dto.OrderStatusRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderService {
    OrderDto placeOrder(Long userID, String shippingAddress);

    Page<OrderDto> findAll(Pageable pageable, Long userId);

    OrderDto update(OrderStatusRequest request, Long id, Long userId);
}
