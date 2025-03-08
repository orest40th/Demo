package com.mate.academy.demo.controller;

import com.mate.academy.demo.dto.CartItemRequestDto;
import com.mate.academy.demo.dto.CartItemRequestDtoWithoutId;
import com.mate.academy.demo.dto.ShoppingCartDto;
import com.mate.academy.demo.mapper.ShoppingCartMapper;
import com.mate.academy.demo.service.ShoppingCartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class ShoppingCartController {
    private final ShoppingCartService service;
    private final ShoppingCartMapper mapper;

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @PostMapping()
    public ShoppingCartDto purchase(@RequestBody CartItemRequestDto cartItem,
                                    Authentication authentication) {
        return mapper.toDto(service.fillCart(cartItem, authentication.getName()));
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @PutMapping("items/{cartItemId}")
    public ShoppingCartDto updateContent(@PathVariable Long cartItemId,
                                         @RequestBody CartItemRequestDtoWithoutId cartItem,
                                      Authentication authentication) {
        return mapper.toDto(service.updateContent(cartItemId, cartItem, authentication.getName()));
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @DeleteMapping("/items/{cartItemId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeContent(@PathVariable Long cartItemId, Authentication authentication) {
        service.removeContent(cartItemId, authentication.getName());
    }
}
