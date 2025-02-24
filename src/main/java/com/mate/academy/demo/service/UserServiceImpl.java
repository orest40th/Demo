package com.mate.academy.demo.service;

import com.mate.academy.demo.dto.UserRegistrationRequestDto;
import com.mate.academy.demo.dto.UserResponseDto;
import com.mate.academy.demo.exception.RegistrationException;
import com.mate.academy.demo.mapper.UserMapper;
import com.mate.academy.demo.model.Role;
import com.mate.academy.demo.model.User;
import com.mate.academy.demo.repository.RoleRepository;
import com.mate.academy.demo.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final UserMapper mapper;

    private final PasswordEncoder encoder;

    @Override
    public UserResponseDto register(UserRegistrationRequestDto requestDto)
            throws RegistrationException {
        if (userRepository.existsByEmail(requestDto.getEmail())) {
            throw new RegistrationException(
                    "User already exists by email " + requestDto.getEmail());
        }
        User model = mapper.toModel(requestDto);
        model.setPassword(encoder.encode(requestDto.getPassword()));
        model.setRoles(Set.of(roleRepository.findByName(
                    Role.RoleName.ROLE_USER.name()).orElseThrow(
                    () -> new EntityNotFoundException(
                            "Role not found by name " + Role.RoleName.ROLE_USER)
        )));

        return mapper.toDto(userRepository.save(model));
    }

    public void registerAdministrator() {
        UserRegistrationRequestDto requestDto = new UserRegistrationRequestDto();
        requestDto.setEmail("admin@adminemail.com");
        requestDto.setFirstName("admin");
        requestDto.setLastName("adminsky");
        requestDto.setShippingAddress("38-37 new shipping address dr OH");
        requestDto.setPassword("qwerty12345");
        requestDto.setRepeatPassword("qwerty12345");

        if (userRepository.existsByEmail(requestDto.getEmail())) {
            return;
        }

        User model = mapper.toModel(requestDto);
        model.setPassword(encoder.encode(requestDto.getPassword()));
        model.setRoles(Set.of(roleRepository.findByName(
                    Role.RoleName.ROLE_ADMIN.name()).orElseThrow(
                    () -> new EntityNotFoundException(
                            "Role not found by name " + Role.RoleName.ROLE_ADMIN)
        )));

        mapper.toDto(userRepository.save(model));
    }
}
