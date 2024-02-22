package UpIn.service;

import java.util.List;

import UpIn.dto.CartDto;
import UpIn.entity.Cart;

public interface CartService
{
    Cart addToCart(Long userId, Long productId, int quantity);
    Cart updateCart(Long cartId, CartDto cartDto);
    void deleteCartItem(Long cartId);
    List<Cart> getCartItems(Long userId);

    // void deleteCartItems(Long userId);
}
