package com.mate.academy.demo.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartItemRequestDto {
    @Positive
    @NotNull
    private Long bookId;
    @Positive
    private int quantity;
}
