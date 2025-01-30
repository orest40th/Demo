package com.mate.academy.demo.dto;

public record BookSearchParametersDto(
        String[] titles,
        String[] authors,
        String[] isbns
) {

}
