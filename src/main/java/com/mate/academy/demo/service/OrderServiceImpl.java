package com.mate.academy.demo.service;

import com.mate.academy.demo.dto.OrderDto;
import com.mate.academy.demo.dto.OrderStatusRequest;
import com.mate.academy.demo.exception.DataProcessingException;
import com.mate.academy.demo.mapper.OrderMapper;
import com.mate.academy.demo.model.Order;
import com.mate.academy.demo.model.ShoppingCart;
import com.mate.academy.demo.repository.OrderRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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
    public Page<OrderDto> findAll(Pageable pageable, Long userId) {
        return repository.findAllByUserId(pageable, userId).map(mapper::toDto);
    }

    @Override
    public OrderDto update(OrderStatusRequest request, Long id, Long userId) {
        Order order = repository.findById(id).orElseThrow(() ->
                new EntityNotFoundException(String.format("Order by id %s not found", id)));
        order.setStatus(request.status());
        return mapper.toDto(repository.save(order));
    }
}
