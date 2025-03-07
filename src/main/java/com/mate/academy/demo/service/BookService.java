package com.mate.academy.demo.service;

import com.mate.academy.demo.dto.BookDto;
import com.mate.academy.demo.dto.BookDtoWithoutCategoryIds;
import com.mate.academy.demo.dto.BookSearchParameters;
import com.mate.academy.demo.dto.CreateBookRequestDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BookService {
    BookDto save(CreateBookRequestDto bookDto);

    Page<BookDto> findAll(Pageable pageable);

    Page<BookDto> search(BookSearchParameters params, Pageable pageable);

    BookDto findById(Long id);

    void deleteById(Long id);

    BookDto updateById(CreateBookRequestDto bookDto, Long id);

    Page<BookDtoWithoutCategoryIds> findAllByCategoryId(Long categoryId, Pageable pageable);
}
