package com.mate.academy.demo.service;

import com.mate.academy.demo.dto.CartItemRequestDto;
import com.mate.academy.demo.dto.ShoppingCartDto;
import com.mate.academy.demo.model.ShoppingCart;
import com.mate.academy.demo.model.User;

public interface ShoppingCartService {
    ShoppingCartDto fillCart(CartItemRequestDto dto, Long id);

    ShoppingCartDto updateContent(Long itemId, CartItemRequestDto cartItem, Long id);

    void removeContent(Long cartItemId, Long id);

    ShoppingCartDto fetchShoppingCart(Long id);

    ShoppingCart save(User user);

    void clearCart(Long id);
}
