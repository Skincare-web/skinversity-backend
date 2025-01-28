package com.skinversity.backend.Controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/product")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
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
    public ResponseEntity<?> getAllProducts() {
        try{
            List<ProductDTO> products = productService.getAllProducts();
            return ResponseEntity.ok(products);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getCategory/{category}")
    public ResponseEntity<?> getByCategory(@PathVariable Category category) {
        try{
            List<ProductDTO> products = productService.getProductsByCategory(category);
            return new ResponseEntity<>(products, HttpStatus.OK);
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("an error ocurred.");
        }
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

    @PutMapping("/update")
    public ResponseEntity<?> updateProduct(
            @RequestParam("product")String updateDetails,
            @RequestParam("newImage")MultipartFile newImage
    )  {
        try{
            ProductDTO productDTO = new ObjectMapper().readValue(updateDetails, ProductDTO.class);
            productService.updateProduct(productDTO, newImage);
        }catch (JsonProcessingException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (IOException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>("Product updated successfully", HttpStatus.OK);
    }

    @PostMapping("/get{productId}")
    public ResponseEntity<?> getProduct(@PathVariable UUID productId) {
        Optional<ProductDTO> product = productService.getProductById(productId);
        if (product.isPresent()) {
            return new ResponseEntity<>(product.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>("Product not found", HttpStatus.NOT_FOUND);
    }
}