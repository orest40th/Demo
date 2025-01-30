package com.mate.academy.demo.service;

import com.mate.academy.demo.dto.BookDto;
import com.mate.academy.demo.dto.CreateBookRequestDto;
import java.util.List;

public interface BookService {
    BookDto save(CreateBookRequestDto bookDto);

    List<BookDto> findAll();

    BookDto findById(Long id);

    void deleteById(Long id);

    BookDto updateById(CreateBookRequestDto bookDto, Long id);
}
