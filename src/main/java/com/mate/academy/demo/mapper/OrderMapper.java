package com.mate.academy.demo.mapper;

import com.mate.academy.demo.config.MapperConfig;
import com.mate.academy.demo.dto.OrderDto;
import com.mate.academy.demo.model.Order;
import com.mate.academy.demo.model.ShoppingCart;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class, uses = OrderItemMapper.class)
public interface OrderMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "total",
            source = "cartItems",
            qualifiedByName = "getTotalFromCartItems")
    @Mapping(target = "shippingAddress", source = "user.shippingAddress")
    @Mapping(target = "orderItems",
            source = "cartItems",
            qualifiedByName = "toOrderItemSetFromCartItemSet")
    Order toOrder(ShoppingCart shoppingCart);

    @AfterMapping
    default void bindOrderItems(@MappingTarget Order order) {
        order.getOrderItems().forEach(orderItem -> orderItem.setOrder(order));
    }

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "orderItems",
            source = "orderItems",
            qualifiedByName = "toOrderItemDtoSetFromModelSet")
    OrderDto toDto(Order order);
}
