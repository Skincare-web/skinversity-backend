package com.skinversity.backend.Controllers;

import com.skinversity.backend.DTOs.ProductDTO;
import com.skinversity.backend.Enumerators.Category;
import com.skinversity.backend.Exceptions.ProductNotFound;
import com.skinversity.backend.Requests.AddProductRequest;
import com.skinversity.backend.Services.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/product")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/addProduct")
    public ResponseEntity<?> addProduct(
            @RequestPart("product")AddProductRequest addProductRequest,
            @RequestPart("productImage")MultipartFile imageFile
    ){
        try {
            productService.addProduct(addProductRequest, imageFile);
            return new ResponseEntity<>("Product added successfully", HttpStatus.OK);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/allProducts")
    public List<ProductDTO> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/getCategory")
    public List<ProductDTO> getBuCategory(Category category) {
        return productService.getProductsByCategory(category);
    }

    @DeleteMapping("/remove-product")
    public ResponseEntity<?> removeProduct(UUID productId) {
        try {
            productService.deleteProduct(productId);
        } catch (ProductNotFound e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>("Product removed successfully", HttpStatus.OK);
    }
}