package com.mate.academy.demo.mapper;

import com.mate.academy.demo.config.MapperConfig;
import com.mate.academy.demo.dto.CategoryDto;
import com.mate.academy.demo.model.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface CategoryMapper {
    CategoryDto toDto(Category category);

    @Mapping(target = "deleted", ignore = true)
    Category toEntity(CategoryDto categoryDto);

    @Mapping(target = "deleted", ignore = true)
    void updateCategoryFromDto(CategoryDto dto, @MappingTarget Category category);
}
