package com.mate.academy.demo.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

@Getter
public class UserLoginRequestDto {
    @Email
    private String email;

    @NotBlank
    @Length(min = 8, max = 25)
    private String password;
}
