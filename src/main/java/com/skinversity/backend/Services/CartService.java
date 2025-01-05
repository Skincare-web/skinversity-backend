package com.skinversity.backend.Services;

import com.skinversity.backend.Models.Cart;
import com.skinversity.backend.Models.Product;
import com.skinversity.backend.Models.Users;
import com.skinversity.backend.Repositories.CartRepository;
import com.skinversity.backend.Repositories.ProductRepository;
import com.skinversity.backend.Repositories.UserRepository;
import com.skinversity.backend.Requests.AddToCartRequest;
import com.skinversity.backend.ServiceInterfaces.CartServiceInterface;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CartService implements CartServiceInterface {
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final CartRepository cartRepository;

    public CartService(UserRepository userRepository, ProductRepository productRepository, CartRepository cartRepository) {
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.cartRepository = cartRepository;
    }

    @Override
    public Cart addItemToCart(AddToCartRequest request) {

        Optional<Users> user = userRepository.findById(request.getUserId());
        Optional<Product> product = productRepository.findById(request.getProductID());
        int quantity = request.getQuantity();

        if (user.isEmpty() || product.isEmpty()) {
            throw new IllegalArgumentException("Item not found");
        }
        Users users = user.get();
        Product products = product.get();

        BigDecimal price = products.getProductPrice().multiply(BigDecimal.valueOf(quantity));

        //create the cart
        Cart cart = new Cart();
        cart.setUser(users);
        cart.setProduct(products);
        cart.setQuantity(quantity == 0 ? 1 : quantity);
        cart.setCreatedAt(LocalDateTime.now().withSecond(0).withNano(0));
        cart.setPrice(price);
        return cartRepository.save(cart);
    }

    @Override
    public void removeItemFromCart(UUID productId, UUID userId) {
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not Found"));
        Cart cartItem = cartRepository.findByUser_UserIdAndProduct_ProductId(userId, productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));

        if (cartItem.getQuantity() > 1){
            cartItem.setQuantity(cartItem.getQuantity() - 1);
            cartRepository.save(cartItem);
        }else{
            cartRepository.delete(cartItem);
        }

    }

    @Override
    public void clearCart(UUID cartId) {

    }

    @Override
    public List<Product> getCartItems(UUID cartId) {
        return List.of();
    }
}
