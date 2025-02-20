package com.mate.academy.demo.dto;

import com.mate.academy.demo.validation.FieldMatch;
import com.mate.academy.demo.validation.Name;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@FieldMatch(password = "password", repeatPassword = "repeatPassword")
public class UserRegistrationRequestDto {
    @Email
    @NotBlank
    private String email;

    @NotBlank
    @Length(min = 8, max = 25)
    private String password;

    @NotBlank
    @Length(min = 8, max = 25)
    private String repeatPassword;

    @Name
    @Length(max = 25)
    private String firstName;

    @Name
    @Length(max = 25)
    private String lastName;

    @Length(min = 5, max = 100)
    private String shippingAddress;
}
