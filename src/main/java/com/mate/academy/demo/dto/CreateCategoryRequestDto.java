package com.mate.academy.demo.dto;

import com.mate.academy.demo.validation.Description;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateCategoryRequestDto {
    @Description
    private String name;
    @Description
    private String description;
}
