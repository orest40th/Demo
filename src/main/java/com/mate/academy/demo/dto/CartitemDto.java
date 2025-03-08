package com.mate.academy.demo.dto;

public record CartitemDto(long id,
                          long bookId,
                          String bookTitle,
                          int quantity) {
}
