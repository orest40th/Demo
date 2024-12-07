package com.mate.academy.demo.service;

import com.mate.academy.demo.dto.BookDto;
import com.mate.academy.demo.dto.CreateBookRequestDto;
import com.mate.academy.demo.mapper.BookMapper;
import com.mate.academy.demo.repository.BookRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;

    private final BookMapper bookMapper;

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
    public List<BookDto> findAll() {
        return bookRepository.findAll().stream()
                        .map(bookMapper::toDto)
                                .collect(Collectors.toList());
    }
}
