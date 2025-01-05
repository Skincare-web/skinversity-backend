package com.skinversity.backend.ServiceInterfaces;

import com.skinversity.backend.Models.Cart;
import com.skinversity.backend.Models.Product;
import com.skinversity.backend.Requests.AddToCartRequest;

import java.util.List;
import java.util.UUID;

public interface CartServiceInterface {
    Cart addItemToCart(AddToCartRequest request);
    void removeItemFromCart(UUID productId, UUID userId);
    void clearCart(UUID cartId);
    List<Product> getCartItems(UUID cartId);
}
