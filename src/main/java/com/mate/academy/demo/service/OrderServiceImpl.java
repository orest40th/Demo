package com.mate.academy.demo.service;

import com.mate.academy.demo.dto.OrderDto;
import com.mate.academy.demo.dto.OrderItemDto;
import com.mate.academy.demo.dto.OrderStatusRequest;
import com.mate.academy.demo.exception.OrderProcessingException;
import com.mate.academy.demo.mapper.OrderItemMapper;
import com.mate.academy.demo.mapper.OrderMapper;
import com.mate.academy.demo.model.Order;
import com.mate.academy.demo.model.OrderItem;
import com.mate.academy.demo.model.ShoppingCart;
import com.mate.academy.demo.repository.OrderItemRepository;
import com.mate.academy.demo.repository.OrderRepository;
import com.mate.academy.demo.repository.ShoppingCartRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderServiceImpl implements OrderService {
    private final ShoppingCartService cartService;
    private final OrderMapper mapper;
    private final OrderItemMapper itemMapper;
    private final OrderRepository repository;
    private final OrderItemRepository itemRepository;
    private final ShoppingCartRepository cartRepository;

    @Transactional
    @Override
    public OrderDto placeOrder(Long userId, String shippingAddress) {
        ShoppingCart shoppingCart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("Shopping cart not found by user id %s", userId)
                ));

        if (shoppingCart.getCartItems() == null
                || shoppingCart.getCartItems().isEmpty()) {
            throw new OrderProcessingException(
                    String.format("Your cart by id %s is empty", shoppingCart.getId()));
        }

        Order order = mapper.toOrder(shoppingCart);
        order.setShippingAddress(shippingAddress);
        cartService.clearCart(userId);
        return mapper.toDto(repository.save(order));
    }

    @Override
    public Set<OrderItemDto> findAllItems(Long userId, Long orderId) {
        Order order = repository.findByOrderId(userId, orderId)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("order by id %s not found in your order", orderId)));
        return mapper.toDto(order).orderItems();
    }

    @Override
    public OrderItemDto getSpecificItem(Long userId, Long orderId, Long itemId) {
        OrderItem orderItem = itemRepository.findByIdAndOrderIdAndUserid(userId, orderId, itemId)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("Item by id %s not found in your order", itemId)));

        return itemMapper.toDto(orderItem);
    }

    @Override
    public Page<OrderDto> findAll(Pageable pageable, Long userId) {
        return repository.findAllByUserId(pageable, userId)
                .map(mapper::toDto);
    }

    @Override
    public OrderDto update(OrderStatusRequest request, Long id) {
        Order order = repository.findById(id).orElseThrow(() ->
                new EntityNotFoundException(String.format("Order by id %s not found", id)));
        order.setStatus(request.status());
        return mapper.toDto(repository.save(order));
    }
}
