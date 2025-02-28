package com.mate.academy.demo.controller;

import com.mate.academy.demo.dto.UserLoginRequestDto;
import com.mate.academy.demo.dto.UserLoginResponseDto;
import com.mate.academy.demo.dto.UserRegistrationRequestDto;
import com.mate.academy.demo.dto.UserResponseDto;
import com.mate.academy.demo.exception.RegistrationException;
import com.mate.academy.demo.security.AuthenticationService;
import com.mate.academy.demo.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "User management", description = "Endpoints for user management")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final UserService service;

    private final AuthenticationService authService;

    @Operation(summary = "Register a user", description = "Registers and saves a user to DB")
    @PostMapping("/registration")
    public UserResponseDto registerUser(@RequestBody @Valid UserRegistrationRequestDto requestDto)
            throws RegistrationException {
        return service.register(requestDto);
    }

    @Operation(summary = "Log a user in", description = "JWT based login")
    @PostMapping("/login")
    public UserLoginResponseDto login(@RequestBody UserLoginRequestDto request) {
        return authService.authenticate(request);
    }
}
