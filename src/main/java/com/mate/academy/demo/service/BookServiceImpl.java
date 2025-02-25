package com.mate.academy.demo.service;

import com.mate.academy.demo.dto.BookDto;
import com.mate.academy.demo.dto.BookSearchParameters;
import com.mate.academy.demo.dto.CreateBookRequestDto;
import com.mate.academy.demo.mapper.BookMapper;
import com.mate.academy.demo.model.Book;
import com.mate.academy.demo.repository.BookRepository;
import com.mate.academy.demo.repository.BookSpecificationBuilder;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final BookSpecificationBuilder specBuilder;

    @Override
    public BookDto save(CreateBookRequestDto bookDto) {
        return bookMapper.toDto(bookRepository.save(bookMapper.toModel(bookDto)));
    }

    @Override
    public BookDto findById(Long id) {
        return bookMapper.toDto(bookRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Book not found by id " + id)));
    }

    @Override
    public void deleteById(Long id) {
        bookRepository.deleteById(id);
    }

    @Override
    public BookDto updateById(CreateBookRequestDto bookDto, Long id) {
        Book bookFetched = bookRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Book not found by id " + id));
        bookMapper.updateBookFromDto(bookDto, bookFetched);
        return bookMapper.toDto(bookRepository.save(bookFetched));
    }

    @Override
    public Page<BookDto> findAll(Pageable pageable) {
        return bookRepository.findAll(pageable).map(bookMapper::toDto);
    }

    public Page<BookDto> search(BookSearchParameters params, Pageable pageable) {
        Specification<Book> spec = specBuilder.build(params);
        return bookRepository.findAll(spec, pageable).map(bookMapper::toDto);
    }
}
