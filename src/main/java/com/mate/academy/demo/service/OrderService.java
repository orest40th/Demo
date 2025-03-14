package com.mate.academy.demo.service;

import com.mate.academy.demo.dto.OrderDto;
import com.mate.academy.demo.dto.OrderItemDto;
import com.mate.academy.demo.dto.OrderStatusRequest;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface OrderService {
    OrderDto placeOrder(Long userID, String shippingAddress);

    List<OrderItemDto> findAllItems(Pageable pageable, Long userId, Long orderId);

    List<OrderDto> findAll(Pageable pageable, Long userId);

    OrderItemDto getSpecificItem(Pageable pageable, Long userId, Long orderId, Long itemId);

    OrderDto update(OrderStatusRequest request, Long id);
}
