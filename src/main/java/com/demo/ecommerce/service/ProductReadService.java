package com.demo.ecommerce.service;
import com.demo.ecommerce.entity.Product;
import com.demo.ecommerce.respository.ProductReadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductReadService {
    @Autowired
    private ProductReadRepository productReadRepository;

    // Get all products
    public List<Product> getAllProducts() {
        return productReadRepository.findAll();
    }

    // Get product by id
    public Optional<Product> getProductById(Long id) {
        return productReadRepository.findById(id);
    }

    // Save a new product
    public Product saveProduct(Product product) {
        return productReadRepository.save(product);
    }

    // find All
    public List<Product> findAll() {
        return new ArrayList<Product>(productReadRepository.findAll());
    }

    // Update an existing product
    public Product updateProduct(Long id, Product productDetails) {
        Product product = productReadRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
        product.setName(productDetails.getName());
        product.setDescription(productDetails.getDescription());
        product.setPrice(productDetails.getPrice());
        product.setQuantity(productDetails.getQuantity());
        product.setImageIds(productDetails.getImageIds()); // Sửa đổi để lưu danh sách ID hình ảnh
        return productReadRepository.save(product);
    }

    // Delete a product
    public void deleteProduct(Long id) {
        Product product = productReadRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
        productReadRepository.delete(product);
    }
}
