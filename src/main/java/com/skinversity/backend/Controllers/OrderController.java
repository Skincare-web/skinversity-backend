package com.skinversity.backend.Controllers;

import com.skinversity.backend.Exceptions.EmptyCart;
import com.skinversity.backend.Exceptions.UserNotFoundException;
import com.skinversity.backend.Services.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
        try {
            orderService.checkout(userId);
        } catch (UserNotFoundException | EmptyCart e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("Your checkout was successful", HttpStatus.OK);
    }
}
