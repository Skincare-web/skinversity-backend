package com.skinversity.backend.Services;

import com.skinversity.backend.Exceptions.ProductNotFound;
import com.skinversity.backend.Exceptions.UserNotFoundException;
import com.skinversity.backend.Models.Cart;
import com.skinversity.backend.Models.CartItem;
import com.skinversity.backend.Models.Product;
import com.skinversity.backend.Models.Users;
import com.skinversity.backend.Repositories.CartRepository;
import com.skinversity.backend.Repositories.ProductRepository;
import com.skinversity.backend.Repositories.UserRepository;
import com.skinversity.backend.Requests.AddOrRemoveFromCartRequest;
import com.skinversity.backend.ServiceInterfaces.CartServiceInterface;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CartService implements CartServiceInterface {
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final CartRepository cartRepository;

    public CartService(UserRepository userRepository,
                       ProductRepository productRepository,
                       CartRepository cartRepository) {
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.cartRepository = cartRepository;
    }

    @Override
    @Transactional
    public Cart addItemToCart(AddOrRemoveFromCartRequest request) {
        Users user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new UserNotFoundException("User Not Found"));
        Cart cart = user.getCart();
        if (cart == null) {
            cart = new Cart();
            cart.setUser(user);
            cart.setCartItems(new ArrayList<>());
            cart.setCreatedAt(LocalDateTime.now().withSecond(0).withNano(0));
            user.setCart(cart);
        }

        Product product = productRepository.findById(request.getProductID())
                .orElseThrow(() -> new ProductNotFound("Product Not Found"));


        Optional<CartItem> existingItems = cart.getCartItems()
                .stream()
                .filter(item -> item.getProduct()
                        .getProductId()
                        .equals(request.getProductID())).findFirst();

        if (existingItems.isPresent()) {
            BigDecimal ogPrice = product.getProductPrice();
            CartItem item = existingItems.get();
            item.setQuantity(item.getQuantity() + request.getQuantity());
            item.setPrice(ogPrice.multiply(new BigDecimal(item.getQuantity())));
        }else{
            CartItem item = new CartItem();
            item.setQuantity(request.getQuantity());
            item.setProduct(product);
            item.setPrice(product.getProductPrice().multiply(new BigDecimal(request.getQuantity())));
            item.setCart(cart);
            cart.getCartItems().add(item);
        }
        cartRepository.save(cart);
        userRepository.save(user);
        return cart;
    }

    @Override
    public void removeItemFromCart(AddOrRemoveFromCartRequest request) {
        Users users = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new UserNotFoundException("User Not Found"));
        Cart cart = users.getCart();

        Optional<CartItem> items = cart.getCartItems()
                .stream()
                .filter(cartItem -> cartItem.getProduct()
                        .getProductId()
                        .equals(request.getProductID())).findFirst();
        if (items.isPresent()) {
            CartItem item = items.get();
            if (item.getQuantity() > request.getQuantity()){
                item.setQuantity(item.getQuantity() - request.getQuantity());
            }else{
                cart.getCartItems()
                        .remove(item);
            }
        }
        cartRepository.save(cart);
    }

    @Override
    public void clearCart(UUID userId) {
        Users users = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User Not Found"));
        Cart cart = users.getCart();
        cart.getCartItems().clear();
        cartRepository.save(cart);
    }

    @Override
    public List<CartItem> getCartItems(UUID userId) {
        Users users = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User Not Found"));
        Cart cart = users.getCart();
        return cart.getCartItems()
                .stream()
                .toList();
    }
}