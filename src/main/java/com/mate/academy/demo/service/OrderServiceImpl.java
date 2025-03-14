package com.mate.academy.demo.service;

import com.mate.academy.demo.dto.OrderDto;
import com.mate.academy.demo.exception.DataProcessingException;
import com.mate.academy.demo.mapper.OrderMapper;
import com.mate.academy.demo.model.Order;
import com.mate.academy.demo.model.ShoppingCart;
import com.mate.academy.demo.repository.OrderRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final ShoppingCartService cartService;
    private final OrderMapper mapper;
    private final OrderRepository repository;

    public OrderDto placeOrder(Long userId) {
        ShoppingCart shoppingCart = cartService.fetchShoppingCartModel(userId);
        if (shoppingCart.getCartItems() == null ||
        shoppingCart.getCartItems().isEmpty()) {
            throw new DataProcessingException("Your cart is empty");
        }
        Order order = mapper.toOrder(shoppingCart);
        cartService.clearCart(userId);
        return mapper.toDto(repository.save(order));
    }
}
