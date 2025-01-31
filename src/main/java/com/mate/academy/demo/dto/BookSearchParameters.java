package com.mate.academy.demo.dto;

public record BookSearchParameters(String[] titles, String[] authors, String[] isbns) {
    public enum FieldTypes {
        TITLES,
        AUTHORS,
        ISBNS
    }
}
