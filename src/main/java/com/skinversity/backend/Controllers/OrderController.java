package com.skinversity.backend.Controllers;

import com.skinversity.backend.Enumerators.OrderStatus;
import com.skinversity.backend.Exceptions.EmptyCart;
import com.skinversity.backend.Exceptions.UserNotFoundException;
import com.skinversity.backend.Models.Users;
import com.skinversity.backend.Requests.PaymentResponse;
import com.skinversity.backend.Services.OrderService;
import org.springframework.boot.web.servlet.filter.OrderedFilter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/order")
public class OrderController {
    private final OrderService orderService;
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/checkout")
    public ResponseEntity<?> checkout(@RequestParam UUID userId) {
        PaymentResponse response;
        try {
            response = orderService.checkout(userId);
        } catch (UserNotFoundException | EmptyCart e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/get-order")
    public ResponseEntity<?> getOrder(@RequestParam UUID orderId) {
        try{
            orderService.getOrderById(orderId);
        }catch (RuntimeException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/update-status")
    public ResponseEntity<?> updateStatus(@RequestParam UUID orderId, @RequestParam OrderStatus status) {
        try{
            orderService.updateOrderStatus(orderId, status);
        }catch (RuntimeException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("Order status successfully updated to " + status, HttpStatus.OK);
    }
}

