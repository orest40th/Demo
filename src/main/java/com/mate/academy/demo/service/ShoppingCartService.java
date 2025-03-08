package com.mate.academy.demo.service;

import com.mate.academy.demo.dto.CartItemRequestDto;
import com.mate.academy.demo.dto.CartItemRequestDtoWithoutId;
import com.mate.academy.demo.dto.ShoppingCartDto;

public interface ShoppingCartService {
    ShoppingCartDto fillCart(CartItemRequestDto dto, String email);

    ShoppingCartDto updateContent(Long itemId, CartItemRequestDtoWithoutId cartItem, String email);

    void removeContent(Long cartItemId, String email);

    ShoppingCartDto fetchShoppingCart(String email);
}
