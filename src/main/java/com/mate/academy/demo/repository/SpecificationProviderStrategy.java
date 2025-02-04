package com.mate.academy.demo.repository;

import com.mate.academy.demo.dto.BookSearchParameters;

public interface SpecificationProviderStrategy<T> {
    SpecificationProvider<T> getSpecificationProvider(BookSearchParameters.FieldTypes type);
}
