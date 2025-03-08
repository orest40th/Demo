package com.mate.academy.demo.controller;

import com.mate.academy.demo.dto.CartItemRequestDto;
import com.mate.academy.demo.dto.CartItemRequestDtoWithoutId;
import com.mate.academy.demo.dto.ShoppingCartDto;
import com.mate.academy.demo.mapper.ShoppingCartMapper;
import com.mate.academy.demo.service.ShoppingCartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Shopping cart management", description = "endpoints for cart management")
@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class ShoppingCartController {
    private final ShoppingCartService service;
    private final ShoppingCartMapper mapper;

    @Operation(summary = "Make a purchase",
            description = "fill the current logged user's cart with items")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @PostMapping()
    public ShoppingCartDto purchase(@RequestBody CartItemRequestDto cartItem,
                                    Authentication authentication) {
        return service.fillCart(cartItem, authentication.getName());
    }

    @Operation(summary = "Show shopping cart",
            description = "View your shopping cart contents and details")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @GetMapping()
    public ShoppingCartDto purchase(Authentication authentication) {
        return service.fetchShoppingCart(authentication.getName());
    }

    @Operation(summary = "Refill your shopping cart",
            description = "select a new different number of the same items "
                    + "for the current logged user's cart")

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @PutMapping("items/{cartItemId}")
    public ShoppingCartDto updateContent(@PathVariable Long cartItemId,
                                         @RequestBody CartItemRequestDtoWithoutId cartItem,
                                      Authentication authentication) {
        return service.updateContent(cartItemId, cartItem, authentication.getName());
    }

    @Operation(summary = "Remove an item", description = "Remove an item from"
            + " the current logged user's cart")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @DeleteMapping("/items/{cartItemId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeContent(@PathVariable Long cartItemId, Authentication authentication) {
        service.removeContent(cartItemId, authentication.getName());
    }
}
