package com.skinversity.backend.ServiceInterfaces;

import com.skinversity.backend.Models.Product;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductServiceInterface {
    List<Product> getAllProducts();
    Optional<Product> getProductById(UUID productId);
    Product addProduct(Product product);
    void updateProduct(Product product);
    void deleteProduct(UUID productId);
}
