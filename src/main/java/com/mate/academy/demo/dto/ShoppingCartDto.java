package com.mate.academy.demo.dto;

import java.util.Set;

public record ShoppingCartDto(long id,
                              long userId,
                              Set<CartitemDto> items) {}
