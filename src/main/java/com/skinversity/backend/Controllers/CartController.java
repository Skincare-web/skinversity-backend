package com.skinversity.backend.Controllers;


import com.skinversity.backend.Requests.AddOrRemoveFromCartRequest;
import com.skinversity.backend.Services.CartService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
public class CartController {
    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping("/add")
    public ResponseEntity<?> addToCart(@RequestBody AddOrRemoveFromCartRequest request) {
        try{
            cartService.addItemToCart(request);
            return new ResponseEntity<>("Item added successfully",HttpStatus.OK);
        }catch(IllegalArgumentException e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/remove-item")
    public ResponseEntity<?> removeFromCart(@RequestBody AddOrRemoveFromCartRequest request) {
        try{
            cartService.removeItemFromCart(request);
            return new ResponseEntity<>("Item deleted successfully",HttpStatus.OK);
        }catch(IllegalArgumentException e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }
}
