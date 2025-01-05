package com.skinversity.backend.Controllers;


import com.skinversity.backend.Requests.AddToCartRequest;
import com.skinversity.backend.Services.CartService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/cart")
public class CartController {
    private CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping("/add")
    public ResponseEntity<?> addToCart(@RequestBody AddToCartRequest request) {
        try{
            cartService.addItemToCart(request);
            return new ResponseEntity<>("Item added successfully",HttpStatus.OK);
        }catch(IllegalArgumentException e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/remove-item")
    public ResponseEntity<?> removeFromCart(@RequestBody UUID productId, UUID userId) {
        try{
            cartService.removeItemFromCart(productId, userId);
            return new ResponseEntity<>("Item deleted successfully",HttpStatus.OK);
        }catch(IllegalArgumentException e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }
}
