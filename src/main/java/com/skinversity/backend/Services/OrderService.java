package com.skinversity.backend.Services;

import com.skinversity.backend.Exceptions.EmptyCart;
import com.skinversity.backend.Exceptions.UserNotFoundException;
import com.skinversity.backend.Models.*;
import com.skinversity.backend.Repositories.CartRepository;
import com.skinversity.backend.Repositories.OrderRepository;
import com.skinversity.backend.Repositories.UserRepository;
import com.skinversity.backend.ServiceInterfaces.OrderServiceInterface;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.skinversity.backend.Enumerators.OrderStatus.PENDING;

@Service
public class OrderService implements OrderServiceInterface {
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;

    public OrderService(UserRepository userRepository, OrderRepository orderRepository, CartRepository cartRepository) {
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
        this.cartRepository = cartRepository;
    }

    @Override
    public void checkout(UUID userId) {
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        Cart cart = user.getCart();

        if (cart == null || cart.getCartItems() == null) {
            throw new EmptyCart("Cart is empty");
        }
        Order order = new Order();
        order.setUser(user);
        order.setCreatedAt(LocalDateTime.now().withSecond(0).withNano(0));
        order.setStatus(PENDING);

        List<OrderItem> checkoutItems = new ArrayList<>();
        for(CartItem cartItem : cart.getCartItems()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(cartItem.getPrice());
            orderItem.setTotal(cartItem.getPrice().multiply(new BigDecimal(cartItem.getQuantity())));
            checkoutItems.add(orderItem);
        }
        order.setOrderItems(checkoutItems);

        cart.getCartItems().clear();

        cartRepository.save(cart);

        orderRepository.save(order);

    }

    @Override
    public Optional<Order> getOrderById(UUID id) {
        return null;
    }

    @Override
    public void updateOrderStatus(Order order) {

    }
}
