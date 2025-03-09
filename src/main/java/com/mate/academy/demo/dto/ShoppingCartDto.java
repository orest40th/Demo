package com.mate.academy.demo.dto;

import java.util.Set;

public record ShoppingCartDto(Long id,
                              Long userId,
                              Set<CartitemDto> items) {}
