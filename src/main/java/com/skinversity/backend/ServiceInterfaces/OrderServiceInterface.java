package com.skinversity.backend.ServiceInterfaces;

import com.skinversity.backend.DTOs.OrderDTO;
import com.skinversity.backend.Models.Order;

import java.util.Optional;
import java.util.UUID;

public interface OrderServiceInterface {
    void placeOrder(OrderDTO order);

    Optional<Order> getOrderById(UUID id);

    void updateOrderStatus(Order order);

}
