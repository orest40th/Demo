package com.mate.academy.demo.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.mate.academy.demo.dto.BookDto;
import com.mate.academy.demo.dto.BookDtoWithoutCategoryIds;
import com.mate.academy.demo.dto.BookSearchParameters;
import com.mate.academy.demo.dto.CreateBookRequestDto;
import com.mate.academy.demo.mapper.impl.BookMapperImpl;
import com.mate.academy.demo.model.Book;
import com.mate.academy.demo.model.Category;
import com.mate.academy.demo.repository.BookRepository;
import com.mate.academy.demo.repository.BookSpecificationBuilder;
import jakarta.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {
    @Mock
    private BookRepository bookRepository;

    @Mock
    private BookMapperImpl bookMapper;

    @Mock
    private BookSpecificationBuilder specBuilder;

    @InjectMocks
    private BookServiceImpl bookService;

    @Test
    @DisplayName("save one book")
    void save_DtoObj_True() {
        CreateBookRequestDto requestDto = new CreateBookRequestDto();
        requestDto.setAuthor("AuthorTest");
        requestDto.setIsbn("IsbnTest");
        requestDto.setPrice(BigDecimal.ONE);
        requestDto.setTitle("TitleTest");
        requestDto.setDescription("DescTest");
        requestDto.setCoverImage("ImgTest");
        requestDto.setCategoryIds(Set.of(1L, 2L));

        Book book = new Book();
        book.setId(1L);
        book.setAuthor(requestDto.getAuthor());
        book.setIsbn(requestDto.getIsbn());
        book.setPrice(requestDto.getPrice());
        book.setTitle(requestDto.getTitle());
        book.setDescription(requestDto.getDescription());
        book.setCoverImage(requestDto.getCoverImage());

        BookDto bookDto = new BookDto();
        bookDto.setId(1L);
        bookDto.setTitle("TitleTest");
        bookDto.setAuthor("AuthorTest");

        Mockito.when(bookMapper.toModel(requestDto)).thenReturn(book);
        Mockito.when(bookRepository.save(book)).thenReturn(book);
        Mockito.when(bookMapper.toDto(book)).thenReturn(bookDto);

        BookDto responseDto = bookService.save(requestDto);

        assertNotNull(responseDto);
        assertEquals(1L, responseDto.getId());
        assertEquals("TitleTest", responseDto.getTitle());
        assertEquals("AuthorTest", responseDto.getAuthor());

        Mockito.verify(bookRepository).save(book);
        Mockito.verify(bookMapper).toModel(requestDto);
        Mockito.verify(bookMapper).toDto(book);
    }

    @Test
    @DisplayName("findById should throw EntityNotFoundException for negative ID")
    void findById_NegativeId_ThrowsEntityNotFoundEx() {
        Long id = -1L;
        Mockito.when(bookRepository.findById(id)).thenReturn(Optional.empty());

        Assertions.assertThrows(EntityNotFoundException.class, () -> {
            bookService.findById(id);
        });
    }

    @Test
    @DisplayName("Delete book by ID")
    void deleteById_ExistingId_Success() {
        Long bookId = 1L;
        Mockito.doNothing().when(bookRepository).deleteById(bookId);
        bookService.deleteById(bookId);
        Mockito.verify(bookRepository, Mockito.times(1)).deleteById(bookId);
    }

    @Test
    @DisplayName("Delete non-existing book ID - should not throw an error")
    void deleteById_NonExistingId_NoException() {
        Long nonExistingId = 999L;
        Mockito.doNothing().when(bookRepository).deleteById(nonExistingId);

        assertDoesNotThrow(() -> bookService.deleteById(nonExistingId));

        Mockito.verify(bookRepository).deleteById(nonExistingId);
        Mockito.verify(bookRepository, Mockito.times(1)).deleteById(nonExistingId);
        Mockito.verify(bookRepository, Mockito.times(0)).findById(nonExistingId);
    }

    @Test
    @DisplayName("")
    void updateById_ExistingID_Success() {
        CreateBookRequestDto requestDto = new CreateBookRequestDto();
        requestDto.setAuthor("AuthorTest");
        requestDto.setIsbn("IsbnTest");
        requestDto.setPrice(BigDecimal.ONE);
        requestDto.setTitle("TitleTest");
        requestDto.setDescription("DescTest");
        requestDto.setCoverImage("ImgTest");
        Category category1 = new Category(1L);
        Category category2 = new Category(2L);
        requestDto.setCategoryIds(Set.of(category1.getId(),
                category2.getId()));

        Book bookToUpdate = new Book();
        bookToUpdate.setId(1L);

        Book bookUpdated = new Book();
        bookUpdated.setAuthor("AuthorTest");
        bookUpdated.setIsbn("IsbnTest");
        bookUpdated.setPrice(BigDecimal.ONE);
        bookUpdated.setTitle("TitleTest");
        bookUpdated.setDescription("DescTest");
        bookUpdated.setCoverImage("ImgTest");
        bookUpdated.setCategories(Set.of(category1, category2));

        BookDto bookDto = new BookDto();
        bookDto.setId(1L);
        bookDto.setTitle("TitleTest");
        bookDto.setAuthor("AuthorTest");
        bookDto.setIsbn("IsbnTest");
        bookDto.setCategoryIds(Set.of(category1.getId(), category2.getId()));
        Long bookId = 1L;

        Mockito.when(bookRepository.findById(bookId)).thenReturn(Optional.of(bookToUpdate));
        Mockito.doNothing().when(bookMapper).updateBookFromDto(requestDto, bookToUpdate);
        Mockito.when(bookRepository.save(bookToUpdate)).thenReturn(bookUpdated);
        Mockito.when(bookMapper.toDto(bookUpdated)).thenReturn(bookDto);

        BookDto responseDto = bookService.updateById(requestDto, bookId);

        assertNotNull(responseDto);
        assertEquals(bookId, responseDto.getId());
        assertEquals(requestDto.getTitle(), responseDto.getTitle());
        assertEquals(requestDto.getIsbn(), responseDto.getIsbn());
        assertEquals(requestDto.getCategoryIds().size(), responseDto.getCategoryIds().size());

        Mockito.verify(bookRepository,
                Mockito.times(1))
                .findById(bookDto.getId());
        Mockito.verify(bookRepository,
                        Mockito.times(1))
                .save(bookToUpdate);
        Mockito.verify(bookMapper, Mockito.times(1)).updateBookFromDto(requestDto, bookToUpdate);
        Mockito.verify(bookRepository).save(bookToUpdate);
    }

    @Test
    @DisplayName("Find all books by category ID")
    void findAllByCategoryId_ValidCategoryId_ReturnsBookDtos() {
        Book book1 = new Book();
        book1.setId(1L);
        book1.setTitle("Book 1");

        Book book2 = new Book();
        book2.setId(2L);
        book2.setTitle("Book 2");

        Page<Book> booksPage = new PageImpl<>(List.of(book1, book2));
        Long categoryId = 1L;
        Pageable pageable = PageRequest.of(0,10);
        Mockito.when(bookRepository.findAllByCategoryId(categoryId, pageable))
                .thenReturn(booksPage);

        BookDtoWithoutCategoryIds dto1 = new BookDtoWithoutCategoryIds();
        dto1.setId(1L);
        dto1.setTitle("Book 1");

        BookDtoWithoutCategoryIds dto2 = new BookDtoWithoutCategoryIds();
        dto2.setId(2L);
        dto2.setTitle("Book 2");

        Mockito.when(bookMapper.toDtoWithoutCategories(book1)).thenReturn(dto1);
        Mockito.when(bookMapper.toDtoWithoutCategories(book2)).thenReturn(dto2);

        Page<BookDtoWithoutCategoryIds> result = bookService
                .findAllByCategoryId(categoryId, pageable);

        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
        assertEquals(2, result.getContent().size());
        assertEquals("Book 1", result.getContent().get(0).getTitle());
        assertEquals("Book 2", result.getContent().get(1).getTitle());

        Mockito.verify(bookRepository, Mockito.times(1))
                .findAllByCategoryId(categoryId, pageable);
        Mockito.verify(bookMapper, Mockito.times(1))
                .toDtoWithoutCategories(book1);
        Mockito.verify(bookMapper, Mockito.times(1))
                .toDtoWithoutCategories(book2);
    }

    @Test
    @DisplayName("Find all books with pagination")
    void findAll_ValidPageable_ReturnsBookDtos() {
        Book book1 = new Book();
        book1.setId(1L);
        book1.setTitle("Book 1");

        Book book2 = new Book();
        book2.setId(2L);
        book2.setTitle("Book 2");

        Page<Book> booksPage = new PageImpl<>(List.of(book1, book2));
        Pageable pageable = PageRequest.of(0, 10);

        Mockito.when(bookRepository.findAll(pageable)).thenReturn(booksPage);

        BookDto dto1 = new BookDto();
        dto1.setId(1L);
        dto1.setTitle("Book 1");

        BookDto dto2 = new BookDto();
        dto2.setId(2L);
        dto2.setTitle("Book 2");

        Mockito.when(bookMapper.toDto(book1)).thenReturn(dto1);
        Mockito.when(bookMapper.toDto(book2)).thenReturn(dto2);

        Page<BookDto> result = bookService.findAll(pageable);

        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
        assertEquals(2, result.getContent().size());
        assertEquals("Book 1", result.getContent().get(0).getTitle());
        assertEquals("Book 2", result.getContent().get(1).getTitle());

        Mockito.verify(bookRepository, Mockito.times(1))
                .findAll(pageable);
        Mockito.verify(bookMapper, Mockito.times(1))
                .toDto(book1);
        Mockito.verify(bookMapper, Mockito.times(1))
                .toDto(book2);
    }

    @Test
    @DisplayName("Search books with parameters and pagination")
    void search_ValidSearchParameters_ReturnsBookDtos() {
        Book book1 = new Book();
        book1.setId(1L);
        book1.setTitle("Book 1");

        Book book2 = new Book();
        book2.setId(2L);
        book2.setTitle("Book 2");

        Page<Book> booksPage = new PageImpl<>(List.of(book1, book2));

        BookSearchParameters params = new BookSearchParameters(new String[] {"Titletest"},
                new String[] {"authorTest"}, new String[] {"isbn"});
        Pageable pageable = PageRequest.of(0, 10);

        Specification<Book> spec = Mockito.mock(Specification.class);
        Mockito.when(specBuilder.build(params)).thenReturn(spec);
        Mockito.when(bookRepository.findAll(spec, pageable)).thenReturn(booksPage);

        BookDto dto1 = new BookDto();
        dto1.setId(1L);
        dto1.setTitle("Book 1");

        BookDto dto2 = new BookDto();
        dto2.setId(2L);
        dto2.setTitle("Book 2");

        Mockito.when(bookMapper.toDto(book1)).thenReturn(dto1);
        Mockito.when(bookMapper.toDto(book2)).thenReturn(dto2);

        Page<BookDto> result = bookService.search(params, pageable);

        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
        assertEquals(2, result.getContent().size());
        assertEquals("Book 1", result.getContent().get(0).getTitle());
        assertEquals("Book 2", result.getContent().get(1).getTitle());

        Mockito.verify(specBuilder, Mockito.times(1))
                .build(params);
        Mockito.verify(bookRepository, Mockito.times(1))
                .findAll(spec, pageable);
        Mockito.verify(bookMapper, Mockito.times(1))
                .toDto(book1);
        Mockito.verify(bookMapper, Mockito.times(1))
                .toDto(book2);
    }
}
