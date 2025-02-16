package com.mate.academy.demo.repository;

import com.mate.academy.demo.dto.BookSearchParameters;
import com.mate.academy.demo.model.Book;
import java.util.List;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class BookSpecificationProviderStrategyImpl implements
        SpecificationProviderStrategy<Book> {
    private final List<SpecificationProvider<Book>> specificationProviderList;

    @Override
    public SpecificationProvider<Book> getSpecificationProvider(
            BookSearchParameters.FieldTypes type) {
        return specificationProviderList
                .stream()
                .filter(provider -> provider.isApplicable(type))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException(
                        "No spec provider for given type: " + type));
    }
}
