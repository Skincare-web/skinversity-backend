package com.skinversity.backend.ServiceInterfaces;

import com.skinversity.backend.Models.Product;

import java.util.List;
import java.util.UUID;

public interface CartServiceInterface {
    void addItemToCart(UUID productId, UUID cartId);
    void removeItemFromCart(UUID productId, UUID cartId);
    void clearCart(UUID cartId);
    List<Product> getCartItems(UUID cartId);
}
