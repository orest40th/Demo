package com.mate.academy.demo.mapper;

import com.mate.academy.demo.config.MapperConfig;
import com.mate.academy.demo.dto.BookDto;
import com.mate.academy.demo.dto.CreateBookRequestDto;
import com.mate.academy.demo.model.Book;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface BookMapper {
    Book toModel(CreateBookRequestDto bookDto);

    default Book toModelComplete(CreateBookRequestDto bookDto, Long id) {
        Book bookModel = toModel(bookDto);
        bookModel.setId(id);

        return bookModel;
    }

    BookDto toDto(Book book);
}
