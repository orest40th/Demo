package com.mate.academy.demo.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.mate.academy.demo.dto.CartItemRequestDto;
import com.mate.academy.demo.dto.ShoppingCartDto;
import com.mate.academy.demo.mapper.impl.ShoppingCartMapperImpl;
import com.mate.academy.demo.model.CartItem;
import com.mate.academy.demo.model.ShoppingCart;
import com.mate.academy.demo.model.User;
import com.mate.academy.demo.repository.BookRepository;
import com.mate.academy.demo.repository.ShoppingCartRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(value = MockitoExtension.class)
public class ShoppingCartServiceTest {
    @Mock
    private ShoppingCartRepository shoppingCartRepository;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private ShoppingCartMapperImpl mapper;

    @InjectMocks
    private ShoppingCartServiceImpl shoppingCartService;

    @Test
    @DisplayName("Throw exception when shopping cart not found")
    void fillCart_ShoppingCartNotFound_ThrowsEntityNotFoundException() {
        CartItemRequestDto cartItemRequestDto = new CartItemRequestDto();
        cartItemRequestDto.setBookId(1L);
        cartItemRequestDto.setQuantity(2);
        Long userId = 1L;

        Mockito.when(shoppingCartRepository.findByUserId(userId)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            shoppingCartService.fillCart(cartItemRequestDto, userId);
        });

        assertEquals("Shopping cart not found by user id 1", exception.getMessage());

        Mockito.verify(shoppingCartRepository,
                Mockito.times(1)).findByUserId(userId);
        Mockito.verify(bookRepository,
                Mockito.times(0)).findById(Mockito.anyLong());
        Mockito.verify(shoppingCartRepository,
                Mockito.times(0)).save(Mockito.any(ShoppingCart.class));
    }

    @Test
    @DisplayName("Throw exception when book not found")
    void fillCart_BookNotFound_ThrowsEntityNotFoundException() {
        CartItemRequestDto cartItemRequestDto = new CartItemRequestDto();
        cartItemRequestDto.setBookId(1L);
        cartItemRequestDto.setQuantity(2);
        User user = new User();
        user.setId(1L);

        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUser(user);
        Mockito.when(shoppingCartRepository.findByUserId(user.getId()))
                .thenReturn(Optional.of(shoppingCart));

        Mockito.when(bookRepository.findById(cartItemRequestDto.getBookId()))
                .thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            shoppingCartService.fillCart(cartItemRequestDto, user.getId());
        });

        assertEquals("Book not found by id 1 ", exception.getMessage());

        Mockito.verify(shoppingCartRepository,
                Mockito.times(1)).findByUserId(user.getId());
        Mockito.verify(bookRepository,
                Mockito.times(1)).findById(cartItemRequestDto.getBookId());
        Mockito.verify(shoppingCartRepository,
                Mockito.times(0)).save(Mockito.any(ShoppingCart.class));
    }

    @Test
    @DisplayName("Update cart item content successfully")
    void updateContent_SuccessfulUpdate_ReturnsUpdatedShoppingCartDto() {
        User user = new User();
        user.setId(1L);
        CartItemRequestDto cartItemRequestDto = new CartItemRequestDto();
        cartItemRequestDto.setQuantity(3);

        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setId(1L);
        shoppingCart.setUser(user);

        Long itemId = 1L;
        CartItem cartItem = new CartItem();
        cartItem.setId(itemId);
        cartItem.setQuantity(2);
        shoppingCart.setCartItems(new HashSet<>(Collections.singletonList(cartItem)));

        Mockito.when(shoppingCartRepository.findByUserId(user.getId()))
                .thenReturn(Optional.of(shoppingCart));

        ShoppingCartDto shoppingCartDto
                = new ShoppingCartDto(1L, user.getId(), new HashSet<>());
        Mockito.when(mapper.toDto(shoppingCart)).thenReturn(shoppingCartDto);
        Mockito.when(shoppingCartRepository.save(shoppingCart)).thenReturn(shoppingCart);
        ShoppingCartDto result = shoppingCartService
                .updateContent(itemId, cartItemRequestDto, user.getId());

        assertNotNull(result);
        assertEquals(3, cartItem.getQuantity());

        Mockito.verify(shoppingCartRepository,
                Mockito.times(1)).findByUserId(user.getId());
        Mockito.verify(shoppingCartRepository,
                Mockito.times(1)).save(shoppingCart);
        Mockito.verify(mapper, Mockito.times(1)).toDto(shoppingCart);
    }

    @Test
    @DisplayName("Remove cart item successfully")
    void removeContent_SuccessfulRemoval_SavesUpdatedShoppingCart() {
        Long cartItemId = 1L;
        User user = new User();
        user.setId(1L);

        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUser(user);
        CartItem cartItem = new CartItem();
        cartItem.setId(cartItemId);
        shoppingCart.setCartItems(new HashSet<>(Collections.singletonList(cartItem)));
        Long userId = 1L;

        Mockito.when(shoppingCartRepository.findByUserId(userId))
                .thenReturn(Optional.of(shoppingCart));

        shoppingCartService.removeContent(cartItemId, userId);

        assertTrue(shoppingCart.getCartItems().isEmpty());

        Mockito.verify(shoppingCartRepository,
                Mockito.times(1)).findByUserId(userId);
        Mockito.verify(shoppingCartRepository,
                Mockito.times(1)).save(shoppingCart);
    }

    @Test
    @DisplayName("Fetch shopping cart successfully")
    void fetchShoppingCart_ExistingUserId_ReturnsShoppingCartDto() {
        User user = new User();
        user.setId(1L);
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUser(user);

        ShoppingCartDto shoppingCartDto = new ShoppingCartDto(1L, 1L, new HashSet<>());

        Mockito.when(shoppingCartRepository.findByUserId(1L))
                .thenReturn(Optional.of(shoppingCart));
        Mockito.when(mapper.toDto(shoppingCart)).thenReturn(shoppingCartDto);

        ShoppingCartDto result = shoppingCartService.fetchShoppingCart(1L);

        assertNotNull(result);
        assertEquals(user.getId(), result.userId());

        Mockito.verify(shoppingCartRepository,
                Mockito.times(1)).findByUserId(1L);
        Mockito.verify(mapper,
                Mockito.times(1)).toDto(shoppingCart);
    }

    @Test
    void clearCart_ExistingUserId_ClearsCartSuccessfully() {
        User user = new User();
        user.setId(1L);
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUser(user);
        Set<CartItem> set = new HashSet<>();
        set.add(new CartItem());
        shoppingCart.setCartItems(set);

        Mockito.when(shoppingCartRepository.findByUserId(user.getId()))
                .thenReturn(Optional.of(shoppingCart));

        shoppingCartService.clearCart(user.getId());

        assertTrue(shoppingCart.getCartItems().isEmpty());
        Mockito.verify(shoppingCartRepository,
                Mockito.times(1)).save(shoppingCart);
    }

}
