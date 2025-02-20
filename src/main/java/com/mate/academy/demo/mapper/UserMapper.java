package com.mate.academy.demo.mapper;

import com.mate.academy.demo.config.MapperConfig;
import com.mate.academy.demo.dto.UserRegistrationRequestDto;
import com.mate.academy.demo.dto.UserResponseDto;
import com.mate.academy.demo.model.User;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface UserMapper {
    UserResponseDto toDto(User user);

    User toModel(UserRegistrationRequestDto requestDto);
}
