package com.mate.academy.demo.service;

import com.mate.academy.demo.model.Book;
import java.util.List;

public interface BookService {
    Book save(Book book);

    List<Book> findAll();
}
