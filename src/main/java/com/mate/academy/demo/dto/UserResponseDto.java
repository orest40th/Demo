package com.mate.academy.demo.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponseDto {
    private String email;

    private String firstName;

    private String lastName;

    private  String shippingAddress;
}
