package com.mate.academy.demo.mapper;

import com.mate.academy.demo.config.MapperConfig;
import com.mate.academy.demo.dto.CartitemDto;
import com.mate.academy.demo.model.CartItem;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(config = MapperConfig.class)
public interface CartItemMapper {
    @Mapping(target = "bookId", source = "book.id")
    @Mapping(target = "bookTitle", source = "book.title")
    CartitemDto toDto(CartItem model);

    @Named("dtoSetFromModel")
    default Set<CartitemDto> toDtoSet(Set<CartItem> modelSet) {
        return modelSet
                .stream()
                .map(this::toDto)
                .collect(Collectors.toSet());
    }
}
