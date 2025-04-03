package com.mate.academy.demo.controller;

import com.mate.academy.demo.dto.BookDto;
import com.mate.academy.demo.dto.BookSearchParameters;
import com.mate.academy.demo.dto.CreateBookRequestDto;
import com.mate.academy.demo.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Book management", description = "endpoints for book management")
@RequestMapping("/books")
@RestController
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @Operation(summary = "Get all books", description = "Retrieves a page of all available books.")
    @GetMapping
    public Page<BookDto> getAll(Pageable pageable) {
        return bookService.findAll(pageable);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @Operation(summary = "Get a book by id", description = "Retrieves a book by id")
    @GetMapping("/{id}")
    public BookDto getBookById(@PathVariable Long id) {
        return bookService.findById(id);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @Operation(summary = "Search all books",
            description = "Retrieves a sorted page of all available books with pagination")
    @GetMapping("/search")
    public Page<BookDto> searchBooks(BookSearchParameters params, Pageable pageable) {
        return bookService.search(params, pageable);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Create a book", description = "Creates a new book in the db")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public BookDto createBook(@Valid @RequestBody CreateBookRequestDto bookDto) {
        return bookService.save(bookDto);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Update an existing book",
            description = "Updates existing book parameters")
    @PutMapping("/{id}")
    public BookDto updateBook(@RequestBody @Valid CreateBookRequestDto bookDto,
                              @PathVariable Long id) {
        return bookService.updateById(bookDto, id);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Delete a book", description = "Safe-deletes an existing book in the db")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteBook(@PathVariable Long id) {
        bookService.deleteById(id);
    }
}
