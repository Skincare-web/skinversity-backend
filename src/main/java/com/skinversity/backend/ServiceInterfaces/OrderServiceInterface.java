package com.skinversity.backend.ServiceInterfaces;

import com.skinversity.backend.Models.Order;

import java.util.Optional;
import java.util.UUID;

public interface OrderServiceInterface {
    void checkout(UUID userId);

    Optional<Order> getOrderById(UUID id);

    void updateOrderStatus(Order order);

}
