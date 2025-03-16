package com.mate.academy.demo.mapper;

import com.mate.academy.demo.config.MapperConfig;
import com.mate.academy.demo.dto.OrderItemDto;
import com.mate.academy.demo.model.CartItem;
import com.mate.academy.demo.model.OrderItem;
import java.math.BigDecimal;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(config = MapperConfig.class)
public interface OrderItemMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "price", source = "book.price")
    OrderItem toOrderItem(CartItem cartItem);

    @Named("toOrderItemSetFromCartItemSet")
    default Set<OrderItem> toOrderItemSet(Set<CartItem> cartItems) {
        return cartItems.stream()
                .map(this::toOrderItem)
                .collect(Collectors.toSet());
    }

    @Named("getTotalFromCartItems")
    default BigDecimal getTotalQuantityForOrder(Set<CartItem> cartItems) {
        return cartItems.stream()
                .map(cartItem -> cartItem.getBook()
                        .getPrice()
                        .multiply(new BigDecimal(cartItem.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Mapping(target = "bookId", source = "book.id")
    OrderItemDto toDto(OrderItem order);

    @Named("toOrderItemDtoSetFromModelSet")
    default Set<OrderItemDto> toOrderItemDtoSet(Set<OrderItem> items) {
        return items.stream()
                .map(this::toDto)
                .collect(Collectors.toSet());
    }
}
