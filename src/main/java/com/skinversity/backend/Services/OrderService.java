package com.skinversity.backend.Services;

import com.skinversity.backend.Exceptions.EmptyCart;
import com.skinversity.backend.Exceptions.UserNotFoundException;
import com.skinversity.backend.Models.*;
import com.skinversity.backend.Repositories.CartRepository;
import com.skinversity.backend.Repositories.OrderRepository;
import com.skinversity.backend.Repositories.UserRepository;
import com.skinversity.backend.Requests.EmailRequest;
import com.skinversity.backend.ServiceInterfaces.OrderServiceInterface;
import org.apache.logging.log4j.message.StringFormattedMessage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private final EmailService emailService;

    public OrderService(UserRepository userRepository, OrderRepository orderRepository, CartRepository cartRepository, EmailService emailService) {
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
        this.cartRepository = cartRepository;
        this.emailService = emailService;
    }

    @Transactional
    @Override
    public void checkout(UUID userId) {
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        Cart cart = user.getCart();

        if (cart == null || cart.getCartItems().isEmpty()) {
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
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(cartItem.getPrice());
            orderItem.setTotal(cartItem.getPrice());
            checkoutItems.add(orderItem);
        }
        order.setOrderItems(checkoutItems);
        order.setTotalPrice(checkoutItems.stream()
                .map(OrderItem::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add));
        orderRepository.save(order);


        cart.getCartItems().clear();
        cartRepository.save(cart);

        //todo email confirmation for placed orders, fix the deletion of cart items.

        EmailRequest request = new EmailRequest();
        request.setRecipient(user.getEmail());
        request.setSubject( "Order Confirmation");
        request.setBodyText("Hello, " + user.getFullName() + "! Your order has been confirmed!");
        emailService.sendEmail(request);

    }

    @Override
    public Optional<Order> getOrderById(UUID id) {
        return null;
    }

    @Override
    public void updateOrderStatus(Order order) {

    }
}
