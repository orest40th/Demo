package com.mate.academy.demo.controller;

import com.mate.academy.demo.dto.OrderDto;
import com.mate.academy.demo.dto.OrderItemDto;
import com.mate.academy.demo.dto.OrderRequest;
import com.mate.academy.demo.dto.OrderStatusRequest;
import com.mate.academy.demo.model.User;
import com.mate.academy.demo.service.OrderService;
import com.mate.academy.demo.service.ShoppingCartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Order management", description = "Order endpoint management")
@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController {
    private final ShoppingCartService cartService;
    private final OrderService orderService;

    @Operation(summary = "Place an order", description = "Post an order")
    @PreAuthorize("hasAuthority('USER')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderDto placeOrder(@RequestBody @Valid OrderRequest request,
                               Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return orderService.placeOrder(user.getId(), request.shippingAddress());
    }

    @Operation(summary = "Get order history",
            description = "Fetches a sorted and filtered list of orders")
    @PreAuthorize("hasAuthority('USER')")
    @GetMapping
    public List<OrderDto> getOrderHistory(Pageable pageable, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return orderService.findAll(pageable, user.getId());
    }

    @Operation(summary = "Get order items", description = "fetches items from an order")
    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("{orderId}/items")
    public List<OrderItemDto> getItemsFromOrder(@PathVariable Long orderId,
                                                Authentication authentication,
                                                Pageable pageable) {
        User user = (User) authentication.getPrincipal();
        return orderService.findAllItems(pageable, user.getId(), orderId);

    }

    @Operation(summary = "Get order item", description = "Fetches a specific order item")
    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("{orderId}/items/{id}")
    public OrderItemDto getSpecificItem(@PathVariable Long orderId,
                                                @PathVariable Long id,
                                                Authentication authentication,
                                                Pageable pageable) {
        User user = (User) authentication.getPrincipal();
        return orderService.getSpecificItem(pageable, user.getId(), orderId, id);
    }

    @Operation(summary = "Update order", description = "Resets order status")
    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/{id}")
    public OrderDto updateStatus(@RequestBody @Valid OrderStatusRequest request,
                                    @PathVariable Long id,
                                    Authentication authentication) {
        return orderService.update(request, id);
    }
}
