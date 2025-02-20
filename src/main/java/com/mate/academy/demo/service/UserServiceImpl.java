package com.mate.academy.demo.service;

import com.mate.academy.demo.dto.UserRegistrationRequestDto;
import com.mate.academy.demo.dto.UserResponseDto;
import com.mate.academy.demo.exception.RegistrationException;
import com.mate.academy.demo.mapper.UserMapper;
import com.mate.academy.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository repository;

    private final UserMapper mapper;

    @Override
    public UserResponseDto save(UserRegistrationRequestDto requestDto)
            throws RegistrationException {
        if (repository.findByEmail(requestDto.getEmail()).isPresent()) {
            throw new RegistrationException("User by this email already exis");
        }
        return mapper.toDto(repository.save(mapper.toModel(requestDto)));
    }
}
