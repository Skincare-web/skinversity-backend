package com.skinversity.backend.Services;

import com.skinversity.backend.DTOs.OrderDTO;
import com.skinversity.backend.Models.Order;
import com.skinversity.backend.ServiceInterfaces.OrderServiceInterface;

import java.util.Optional;
import java.util.UUID;

public class OrderService implements OrderServiceInterface {
    @Override
    public void placeOrder(OrderDTO order) {

    }

    @Override
    public Optional<Order> getOrderById(UUID id) {
        return null;
    }

    @Override
    public void updateOrderStatus(Order order) {

    }
}
