package com.mate.academy.demo.service;

import com.mate.academy.demo.dto.UserRegistrationRequestDto;
import com.mate.academy.demo.dto.UserResponseDto;
import com.mate.academy.demo.exception.RegistrationException;
import com.mate.academy.demo.mapper.UserMapper;
import com.mate.academy.demo.model.Role;
import com.mate.academy.demo.model.User;
import com.mate.academy.demo.repository.RoleRepository;
import com.mate.academy.demo.repository.UserRepository;
import jakarta.transaction.Transactional;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ShoppingCartService shoppingCartService;
    private final UserMapper mapper;
    private final PasswordEncoder encoder;

    @Transactional
    @Override
    public UserResponseDto register(UserRegistrationRequestDto requestDto)
            throws RegistrationException {
        if (userRepository.existsByEmail(requestDto.getEmail())) {
            throw new RegistrationException(
                    "User already exists by email " + requestDto.getEmail());
        }

        User model = mapper.toModel(requestDto);
        Role.RoleName userRole = Role.RoleName.USER;
        model.setPassword(encoder.encode(requestDto.getPassword()));
        model.setRoles(Set.of(roleRepository.findByName(userRole)));
        UserResponseDto dto = mapper.toDto(userRepository.save(model));
        shoppingCartService.save(model);
        return dto;
    }
}
