package com.skinversity.backend.Controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.skinversity.backend.DTOs.ProductDTO;
import com.skinversity.backend.Enumerators.Category;
import com.skinversity.backend.Exceptions.ProductNotFound;
import com.skinversity.backend.Requests.AddProductRequest;
import com.skinversity.backend.Services.CloudinaryService;
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
    private final CloudinaryService cloudinaryService;

    public ProductController(ProductService productService, CloudinaryService cloudinaryService) {
        this.productService = productService;
        this.cloudinaryService = cloudinaryService;
    }

    @PostMapping("/addProduct")
    public ResponseEntity<?> addProduct(
            @RequestParam("product")String productJson,
            @RequestParam("productImage")MultipartFile productImage
    ){
        try {
            AddProductRequest addProductRequest = new ObjectMapper().readValue(productJson, AddProductRequest.class);
            productService.addProduct(addProductRequest, productImage);
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

    @GetMapping("/getCategory/{category}")
    public List<ProductDTO> getByCategory(@PathVariable Category category) {
        return productService.getProductsByCategory(category);
    }

    @DeleteMapping("/remove-product/{productId}")
    public ResponseEntity<?> removeProduct(@PathVariable UUID productId) {
        try {
            productService.deleteProduct(productId);
        } catch (ProductNotFound e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>("Product removed successfully", HttpStatus.OK);
    }
/*    @PostMapping("/uploadImage")
    public ResponseEntity<?> uploadImage(MultipartFile file) {
        try {
            cloudinaryService.uploadFile(file);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>("Image uploaded successfully", HttpStatus.OK);
    }

    @PostMapping("/putProduct")
    public ResponseEntity<?> putProduct(@RequestBody AddProductRequest request){
        productService.addProductNoPicture(request);
        return new ResponseEntity<>("Product added successfully", HttpStatus.OK);
    }*/
}