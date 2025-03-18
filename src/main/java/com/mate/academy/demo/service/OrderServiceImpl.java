package com.mate.academy.demo.service;

import com.mate.academy.demo.dto.OrderDto;
import com.mate.academy.demo.dto.OrderItemDto;
import com.mate.academy.demo.dto.OrderStatusRequest;
import com.mate.academy.demo.exception.DataProcessingException;
import com.mate.academy.demo.mapper.OrderMapper;
import com.mate.academy.demo.model.Order;
import com.mate.academy.demo.model.ShoppingCart;
import com.mate.academy.demo.repository.OrderRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final ShoppingCartService cartService;
    private final OrderMapper mapper;
    private final OrderRepository repository;

    @Override
    public OrderDto placeOrder(Long userId, String shippingAddress) {
        ShoppingCart shoppingCart = cartService.fetchShoppingCartModel(userId);
        if (shoppingCart.getCartItems() == null
                || shoppingCart.getCartItems().isEmpty()) {
            throw new DataProcessingException("Your cart is empty");
        }

        Order order = mapper.toOrder(shoppingCart);
        order.setShippingAddress(shippingAddress);
        cartService.clearCart(userId);
        return mapper.toDto(repository.save(order));
    }

    @Override
    public List<OrderItemDto> findAllItems(Pageable pageable, Long userId, Long orderId) {
        return repository.findListAllByUserId(pageable, userId)
                .stream()
                .map(mapper::toDto)
                .filter(orderDto -> orderDto.id().equals(orderId))
                .map(OrderDto::orderItems)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    @Override
    public OrderItemDto getSpecificItem(Pageable pageable, Long userId, Long orderId, Long itemId) {
        return repository.findListAllByUserId(pageable, userId)
                .stream()
                .map(mapper::toDto)
                .filter(orderDto -> orderDto.id().equals(orderId))
                .map(OrderDto::orderItems)
                .flatMap(Collection::stream)
                .filter(orderItemDto -> orderItemDto.id().equals(itemId))
                .findAny()
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("Item by id %s not found in your order", orderId)));
    }

    @Override
    public List<OrderDto> findAll(Pageable pageable, Long userId) {
        return repository.findAllByUserId(pageable, userId).map(mapper::toDto).getContent();
    }

    @Override
    public OrderDto update(OrderStatusRequest request, Long id) {
        Order order = repository.findById(id).orElseThrow(() ->
                new EntityNotFoundException(String.format("Order by id %s not found", id)));
        order.setStatus(request.status());
        return mapper.toDto(repository.save(order));
    }
}
