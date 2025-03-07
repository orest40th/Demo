package com.mate.academy.demo.mapper;

import com.mate.academy.demo.config.MapperConfig;
import com.mate.academy.demo.dto.BookDto;
import com.mate.academy.demo.dto.BookDtoWithoutCategoryIds;
import com.mate.academy.demo.dto.CreateBookRequestDto;
import com.mate.academy.demo.model.Book;
import com.mate.academy.demo.model.Category;
import java.util.stream.Collectors;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface BookMapper {
    @Mapping(target = "categories", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    Book toModel(CreateBookRequestDto bookDto);

    @Mapping(target = "categories", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    void updateBookFromDto(CreateBookRequestDto book, @MappingTarget Book entity);

    @Mapping(target = "categoryIds", ignore = true)
    BookDto toDto(Book book);

    BookDtoWithoutCategoryIds toDtoWithoutCategories(Book book);

    @AfterMapping
    default void setCategoryIds(@MappingTarget BookDto bookDto, Book book) {
        bookDto.setCategoryIds(book.getCategories()
                .stream()
                .map(Category::getId)
                .collect(Collectors.toSet()));
    }

    @AfterMapping
    default void setCategories(@MappingTarget Book book, CreateBookRequestDto dto) {
        book.setCategories(dto.getCategoryIds()
                .stream()
                .map(Category::new)
                .collect(Collectors.toSet()));
    }
}
