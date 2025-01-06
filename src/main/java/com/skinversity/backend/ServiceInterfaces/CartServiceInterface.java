package com.skinversity.backend.ServiceInterfaces;

import com.skinversity.backend.Models.Cart;
import com.skinversity.backend.Models.CartItem;
import com.skinversity.backend.Models.Product;
import com.skinversity.backend.Requests.AddOrRemoveFromCartRequest;

import java.util.List;
import java.util.UUID;

public interface CartServiceInterface {
    Cart addItemToCart(AddOrRemoveFromCartRequest request);
    void removeItemFromCart(AddOrRemoveFromCartRequest request);
    void clearCart(UUID userId);
    List<CartItem> getCartItems(UUID userId);
}
