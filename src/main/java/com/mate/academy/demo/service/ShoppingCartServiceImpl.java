package com.mate.academy.demo.service;

import com.mate.academy.demo.dto.CartItemRequestDto;
import com.mate.academy.demo.dto.ShoppingCartDto;
import com.mate.academy.demo.mapper.ShoppingCartMapper;
import com.mate.academy.demo.model.Book;
import com.mate.academy.demo.model.CartItem;
import com.mate.academy.demo.model.ShoppingCart;
import com.mate.academy.demo.model.User;
import com.mate.academy.demo.repository.BookRepository;
import com.mate.academy.demo.repository.ShoppingCartRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Transactional
@Service
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final BookRepository bookRepository;
    private final ShoppingCartMapper mapper;

    @Override
    public ShoppingCartDto fillCart(CartItemRequestDto cartItemRequestDto, Long userId) {
        ShoppingCart shoppingCart = shoppingCartRepository
                .findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("Shopping cart not found by user id %s", userId)));
        Book book = bookRepository
                .findById(cartItemRequestDto.getBookId())
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("Book not found by id %s ", cartItemRequestDto.getBookId())));
        CartItem item = new CartItem();
        item.setBook(book);
        item.setShoppingCart(shoppingCart);
        item.setQuantity(cartItemRequestDto.getQuantity());
        addItem(shoppingCart, item, cartItemRequestDto.getQuantity());

        return mapper.toDto(shoppingCartRepository.save(shoppingCart));
    }

    @Override
    public ShoppingCartDto updateContent(Long itemId,
                                      CartItemRequestDto cartItem,
                                      Long userId) {
        ShoppingCart shoppingCart = shoppingCartRepository
                .findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("Shopping cart not found by user id %s ", userId)));
        updateItem(shoppingCart, itemId, cartItem.getQuantity());
        return mapper.toDto(shoppingCartRepository.save(shoppingCart));
    }

    @Override
    public void removeContent(Long cartItemId, Long userId) {
        ShoppingCart shoppingCart = shoppingCartRepository
                .findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("Shopping cart not found by user id %s", userId)));
        deleteItem(shoppingCart, cartItemId);

        shoppingCartRepository.save(shoppingCart);
    }

    @Override
    public ShoppingCartDto fetchShoppingCart(Long userId) {
        return shoppingCartRepository.findByUserId(userId)
                .map(mapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("Shopping cart not found by user id %s", userId)
                ));
    }

    @Override
    public ShoppingCart save(User user) {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUser(user);
        return shoppingCartRepository.save(shoppingCart);
    }

    private void addItem(ShoppingCart shoppingCart, CartItem item, int quantity) {
        Set<CartItem> cartItems = shoppingCart.getCartItems();

        if (cartItems.contains(item)) {
            cartItems.stream()
                    .filter(item::equals)
                    .forEach(cartItem -> cartItem.setQuantity(cartItem.getQuantity() + quantity));
        } else {
            cartItems.add(item);
        }
    }

    private void updateItem(ShoppingCart cart, long itemId, int quantity) {
        CartItem cartItemToUpdate = cart.getCartItems()
                .stream()
                .filter(cartItem -> cartItem.getId().equals(itemId))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("There is no item by id %s in your cart", itemId)));
        cartItemToUpdate.setQuantity(quantity);
    }

    private void deleteItem(ShoppingCart cart, long itemId) {
        CartItem cartItemToRemove = cart.getCartItems()
                .stream()
                .filter(item -> item.getId().equals(itemId))
                .findAny()
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("There is no item by id %s in your cart", itemId)));
        Set<CartItem> cartItemsUpdated = cart.getCartItems();
        cartItemsUpdated.remove(cartItemToRemove);
        cart.setCartItems(cartItemsUpdated);
    }
}
