package com.skinversity.backend.Services;

import com.skinversity.backend.DTOs.ProductDTO;
import com.skinversity.backend.Enumerators.Category;
import com.skinversity.backend.Exceptions.ProductNotFound;
import com.skinversity.backend.Models.Product;
import com.skinversity.backend.Repositories.ProductRepository;
import com.skinversity.backend.ServiceInterfaces.ProductServiceInterface;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class ProductService implements ProductServiceInterface {
    private ProductRepository productRepository;

    @Override
    public List<ProductDTO> getAllProducts() {
        return productRepository.findAll()
                .stream()
                .map(ProductDTO::mapToDTO)
                .toList();
    }

    @Override
    public Optional<ProductDTO> getProductById(UUID productId) {
        return productRepository.findAll()
                .stream()
                .filter(product -> product.getProductId().equals(productId))
                .map(ProductDTO::mapToDTO)
                .findAny();

    }

    @Override
    public ProductDTO addProduct(ProductDTO product) {
        return
    }

    @Override
    public void updateProduct(ProductDTO product) {

    }

    @Override
    public void deleteProduct(UUID productId) {
        Optional<Product> prodToDelete = productRepository.findById(productId);
        if (prodToDelete.isPresent()) {
            productRepository.delete(prodToDelete.get());
        }else{
            throw new ProductNotFound("The product does not exist");
        }
    }

    @Override
    public List<ProductDTO> getProductsByCategory(Category category) {
        return productRepository
                .findByCategory(category)
                .stream()
                .map(ProductDTO::mapToDTO)
                .toList();
    }
}
