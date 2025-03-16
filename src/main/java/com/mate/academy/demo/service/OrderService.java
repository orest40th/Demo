package com.mate.academy.demo.service;

import com.mate.academy.demo.dto.OrderDto;
import com.mate.academy.demo.dto.OrderItemDto;
import com.mate.academy.demo.dto.OrderStatusRequest;
import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderService {
    OrderDto placeOrder(Long userID, String shippingAddress);

    Set<OrderItemDto> findAllItems(Long userId, Long orderId);

    Page<OrderDto> findAll(Pageable pageable, Long userId);

    OrderItemDto getSpecificItem(Long userId, Long orderId, Long itemId);

    OrderDto update(OrderStatusRequest request, Long id);
}
