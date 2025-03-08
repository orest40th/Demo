package com.mate.academy.demo.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartItemRequestDto {
    private Long bookId;
    private int quantity;
}
