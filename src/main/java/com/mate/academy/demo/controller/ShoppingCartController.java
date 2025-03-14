package com.mate.academy.demo.controller;

import com.mate.academy.demo.dto.CartItemRequestDto;
import com.mate.academy.demo.dto.ShoppingCartDto;
import com.mate.academy.demo.model.User;
import com.mate.academy.demo.service.ShoppingCartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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

    @Operation(summary = "Make a purchase",
            description = "fill the current logged user's cart with items")
    @PreAuthorize("hasAuthority('USER')")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ShoppingCartDto purchase(@RequestBody @Valid CartItemRequestDto cartItem,
                                    Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return service.fillCart(cartItem, user.getId());
    }

    @Operation(summary = "Show shopping cart",
            description = "View your shopping cart contents and details")
    @PreAuthorize("hasAuthority('USER')")
    @GetMapping
    public ShoppingCartDto viewContents(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return service.fetchShoppingCart(user.getId());
    }

    @Operation(summary = "Refill your shopping cart",
            description = "select a new different number of the same items "
                    + "for the current logged user's cart")

    @PreAuthorize("hasAuthority('USER')")
    @PutMapping("items/{cartItemId}")
    public ShoppingCartDto updateContent(@PathVariable Long cartItemId,
                                         @RequestBody @Valid CartItemRequestDto cartItem,
                                         Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return service.updateContent(cartItemId, cartItem, user.getId());
    }

    @Operation(summary = "Remove an item", description = "Remove an item from"
            + " the current logged user's cart")
    @PreAuthorize("hasAuthority('USER')")
    @DeleteMapping("/items/{cartItemId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeContent(@PathVariable Long cartItemId, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        service.removeContent(cartItemId, user.getId());
    }
}
