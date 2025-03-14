package com.mate.academy.demo.dto;

public record CartitemDto(Long id,
                          Long bookId,
                          String bookTitle,
                          int quantity) {
}
