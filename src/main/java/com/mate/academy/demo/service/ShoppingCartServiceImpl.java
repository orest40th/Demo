package com.mate.academy.demo.service;

import com.mate.academy.demo.dto.CartItemRequestDto;
import com.mate.academy.demo.dto.CartItemRequestDtoWithoutId;
import com.mate.academy.demo.model.Book;
import com.mate.academy.demo.model.CartItem;
import com.mate.academy.demo.model.ShoppingCart;
import com.mate.academy.demo.repository.BookRepository;
import com.mate.academy.demo.repository.ShoppingCartRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final BookRepository bookRepository;

    @Transactional
    @Override
    public ShoppingCart fillCart(CartItemRequestDto cartItemRequestDto, String email) {
        ShoppingCart cartByUserEmail = shoppingCartRepository
                .findByUserEmail(email)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Shopping cart not foind by user email " + email));
        Book book = bookRepository
                .findById(cartItemRequestDto.getBookId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Book not found by id " + cartItemRequestDto.getBookId()));
        CartItem item = new CartItem();
        item.setBook(book);
        item.setShoppingCart(cartByUserEmail);
        item.setQuantity(cartItemRequestDto.getQuantity());
        addItem(cartByUserEmail, item, cartItemRequestDto.getQuantity());

        return shoppingCartRepository.save(cartByUserEmail);
    }

    private void addItem(ShoppingCart shoppingCart, CartItem item, int quantity) {
        Set<CartItem> cartItems = shoppingCart.getCartItems();

        if (cartItems.contains(item)) {
            cartItems.stream()
                    .filter(item::equals)
                    .forEach(cartItem -> cartItem.setQuantity(cartItem.getQuantity() + quantity));
            return;
        }

        cartItems.add(item);
    }

    @Override
    public ShoppingCart updateContent(Long itemId,
                                      CartItemRequestDtoWithoutId cartItem,
                                      String email) {
        ShoppingCart cartByUserEmail = shoppingCartRepository
                .findByUserEmail(email)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Shopping cart not foind by user email " + email));
        updateItem(cartByUserEmail, itemId, cartItem.getQuantity());
        return shoppingCartRepository.save(cartByUserEmail);
    }

    private void updateItem(ShoppingCart cart, long itemId, int quantity) {
        CartItem cartItemToUpdate = cart.getCartItems()
                .stream()
                .filter(cartItem -> cartItem.getId() == itemId)
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException(
                        "There is no item by id "
                        + itemId
                        + " in your cart"));
        cartItemToUpdate.setQuantity(quantity);
    }

    @Override
    public void removeContent(Long cartItemId, String email) {
        ShoppingCart cartByUserEmail = shoppingCartRepository
                .findByUserEmail(email)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Shopping cart not found by user email " + email));
        deleteItem(cartByUserEmail, cartItemId);

        shoppingCartRepository.save(cartByUserEmail);
    }

    private void deleteItem(ShoppingCart cart, long itemId) {
        CartItem cartItemToRemove = cart.getCartItems()
                .stream()
                .filter(item -> item.getId() == itemId)
                .findAny()
                .orElseThrow(() -> new EntityNotFoundException(
                        "There is no item by id "
                                + itemId
                                + " in your cart"));
        Set<CartItem> cartItemsUpdated = cart.getCartItems();
        cartItemsUpdated.remove(cartItemToRemove);
        cart.setCartItems(cartItemsUpdated);
    }
}
