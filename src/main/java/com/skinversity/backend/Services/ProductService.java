package com.skinversity.backend.Services;

import com.skinversity.backend.DTOs.ProductDTO;
import com.skinversity.backend.Enumerators.Category;
import com.skinversity.backend.Exceptions.ProductNotFound;
import com.skinversity.backend.Models.Product;
import com.skinversity.backend.Repositories.ProductRepository;
import com.skinversity.backend.Requests.AddProductRequest;
import com.skinversity.backend.ServiceInterfaces.ProductServiceInterface;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProductService implements ProductServiceInterface {
    private final ProductRepository productRepository;
    private final CloudinaryService cloudinaryService;

    public ProductService(ProductRepository productRepository, CloudinaryService cloudinaryService) {
        this.productRepository = productRepository;
        this.cloudinaryService = cloudinaryService;
    }


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
    public Product addProduct(AddProductRequest request, MultipartFile image) throws IOException {
        return getProduct(request, image, cloudinaryService, productRepository);
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

    private static Product getProduct(AddProductRequest request,
                                      MultipartFile image,
                                      CloudinaryService cloudinaryService,
                                      ProductRepository productRepository) throws IOException {
        String imageURL = cloudinaryService.uploadFile(image);
        Product product = new Product();
        product.setProductName(request.getProductName());
        product.setProductDescription(request.getProductDescription());
        product.setProductPrice(request.getProductPrice());
        product.setProductSKU(request.getProductSKU());
        product.setCategory(request.getCategory());
        product.setProductImageURL(imageURL);
        product.setProductCreationDate(LocalDateTime.now().withNano(0).withSecond(0));
        product.setProductQuantity(request.getProductQuantity() == 0 ? 1 : request.getProductQuantity());
        product.setCategory(request.getCategory());
        productRepository.save(product);
        return product;
    }

/*    public Product addProductNoPicture(AddProductRequest request) {
        Product product = new Product();
        product.setProductName(request.getProductName());
        product.setProductDescription(request.getProductDescription());
        product.setProductPrice(request.getProductPrice());
        product.setProductSKU(request.getProductSKU());
        product.setCategory(request.getCategory());
        productRepository.save(product);
        return product;
    }*/
}
