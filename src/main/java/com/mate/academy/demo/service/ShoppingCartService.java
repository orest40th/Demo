package com.mate.academy.demo.service;

import com.mate.academy.demo.dto.CartItemRequestDto;
import com.mate.academy.demo.dto.CartItemRequestDtoWithoutId;
import com.mate.academy.demo.model.ShoppingCart;

public interface ShoppingCartService {
    ShoppingCart fillCart(CartItemRequestDto dto, String email);

    ShoppingCart updateContent(Long itemId, CartItemRequestDtoWithoutId cartItem, String email);

    void removeContent(Long cartItemId, String email);
}
