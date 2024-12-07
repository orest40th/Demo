package com.mate.academy.demo.mapper;

import com.mate.academy.demo.dto.BookDto;
import com.mate.academy.demo.dto.CreateBookRequestDto;
import com.mate.academy.demo.model.Book;
import org.mapstruct.Mapper;

@Mapper(config = com.mate.academy.demo.config.MapperConfig.class)
public interface BookMapper {
    Book toModel(CreateBookRequestDto bookDto);

    BookDto toDto(Book book);
}
