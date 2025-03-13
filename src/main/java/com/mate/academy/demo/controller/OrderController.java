package com.mate.academy.demo.controller;

import com.mate.academy.demo.dto.OrderDto;
import com.mate.academy.demo.dto.OrderItemDto;
import com.mate.academy.demo.dto.OrderRequest;
import com.mate.academy.demo.dto.OrderStatusRequest;
import com.mate.academy.demo.dto.ShoppingCartDto;
import com.mate.academy.demo.model.Order;
import com.mate.academy.demo.model.OrderItem;
import com.mate.academy.demo.model.User;
import com.mate.academy.demo.service.OrderService;
import com.mate.academy.demo.service.ShoppingCartService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
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

import javax.swing.text.html.parser.Entity;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController {
    private final ShoppingCartService cartService;
    private final OrderService orderService;

    @PreAuthorize("hasAuthority('USER')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderDto placeOrder(@RequestBody @Valid OrderRequest request,
                               Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return orderService.placeOrder(user.getId(), request.shippingAddress());
    }

    @PreAuthorize("hasAuthority('USER')")
    @GetMapping
    public List<OrderDto> getOrderHistory(Pageable pageable, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return orderService.findAll(pageable, user.getId()).getContent();
    }

    @GetMapping("{orderId}/items")
    public List<OrderItemDto> getItemsFromOrder(@PathVariable Long orderId,
                                                Authentication authentication,
                                                Pageable pageable) {
        User user = (User) authentication.getPrincipal();
        return orderService.findAll(pageable, user.getId())
                .getContent()
                .stream()
                .filter(orderDto -> orderDto.id().equals(orderId))
                .map(OrderDto::orderItems)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    @GetMapping("{orderId}/items/{id}")
    public OrderItemDto getSpecificItem(@PathVariable Long orderId,
                                                @PathVariable Long id,
                                                Authentication authentication,
                                                Pageable pageable) {
        User user = (User) authentication.getPrincipal();
        return orderService.findAll(pageable, user.getId())
                .getContent()
                .stream()
                .filter(orderDto -> orderDto.id().equals(orderId))
                .map(OrderDto::orderItems)
                .flatMap(Collection::stream)
                .filter(orderItemDto -> orderItemDto.id().equals(id))
                .findAny()
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("Item by id %s not found in your order", id)));
    }

    @PreAuthorize("hasAuthority('USER')")
    @PutMapping("/{id}")
    public OrderDto getOrderHistory(@RequestBody @Valid OrderStatusRequest request,
                                    @PathVariable Long id,
                                    Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return orderService.update(request, id, user.getId());
    }
}
