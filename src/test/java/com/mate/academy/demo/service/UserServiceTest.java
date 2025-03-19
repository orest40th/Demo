package com.mate.academy.demo.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.mate.academy.demo.dto.UserRegistrationRequestDto;
import com.mate.academy.demo.dto.UserResponseDto;
import com.mate.academy.demo.exception.RegistrationException;
import com.mate.academy.demo.mapper.impl.UserMapperImpl;
import com.mate.academy.demo.model.Role;
import com.mate.academy.demo.model.User;
import com.mate.academy.demo.repository.RoleRepository;
import com.mate.academy.demo.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private ShoppingCartServiceImpl shoppingCartService;

    @Mock
    private UserMapperImpl mapper;

    @Mock
    private PasswordEncoder encoder;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    @DisplayName("Register new user successfully")
    void register_ValidUserRegistration_ReturnsUserResponseDto() throws RegistrationException {
        UserRegistrationRequestDto requestDto = new UserRegistrationRequestDto();
        requestDto.setEmail("test@example.com");
        requestDto.setPassword("qwerty123");

        Mockito.when(userRepository.existsByEmail(requestDto.getEmail())).thenReturn(false);

        User user = new User();
        Mockito.when(mapper.toModel(requestDto)).thenReturn(user);

        Role userRole = new Role();
        userRole.setRoleName(Role.RoleName.USER);
        Mockito.when(roleRepository.findByName(Role.RoleName.USER)).thenReturn(userRole);
        Mockito.when(encoder.encode(requestDto.getPassword())).thenReturn("encodedPassword123");
        Mockito.when(userRepository.save(user)).thenReturn(user);

        UserResponseDto responseDto = new UserResponseDto();
        responseDto.setEmail(requestDto.getEmail());
        Mockito.when(mapper.toDto(user)).thenReturn(responseDto);
        UserResponseDto result = userService.register(requestDto);

        assertNotNull(result);
        assertEquals(requestDto.getEmail(), result.getEmail());
        assertEquals(requestDto.getEmail(), result.getEmail());
        Mockito.verify(userRepository, Mockito.times(1))
                .existsByEmail(requestDto.getEmail());
        Mockito.verify(userRepository, Mockito.times(1))
                .save(user);
        Mockito.verify(roleRepository, Mockito.times(1))
                .findByName(Role.RoleName.USER);
        Mockito.verify(shoppingCartService, Mockito.times(1)).save(user);
    }
}
