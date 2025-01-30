package com.mate.academy.demo.mapper;

import com.mate.academy.demo.config.MapperConfig;
import com.mate.academy.demo.dto.BookDto;
import com.mate.academy.demo.dto.CreateBookRequestDto;
import com.mate.academy.demo.model.Book;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface BookMapper {
    Book toModel(CreateBookRequestDto bookDto);

    void updateBookFromDto(CreateBookRequestDto book, @MappingTarget Book entity);

    BookDto toDto(Book book);
}
