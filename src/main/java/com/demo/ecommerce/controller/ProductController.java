package com.demo.ecommerce.controller;

import com.demo.ecommerce.data.enums.DataSourceType;
import com.demo.ecommerce.entity.Product;
import com.demo.ecommerce.service.ProductReadService;
import com.demo.ecommerce.service.ProductService;
import com.demo.ecommerce.util.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductReadService productReadService;

    private String getHostName() {
        return System.getenv("HOSTNAME");
    }

    private String getIpAddress(HttpServletRequest request) {
        String ipAddress = request.getHeader("X-Forwarded-For");
        if (ipAddress == null || ipAddress.isEmpty()) {
            ipAddress = request.getRemoteAddr();  // Lấy IP thực từ request
        }
        return ipAddress;
    }

    // Get all products
    @GetMapping
    public ResponseEntity<ApiResponse<Page<Product>>> getAllProducts(
            HttpServletRequest request, 
            @RequestParam String type,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        String hostname = getHostName();
        String ipAddress = getIpAddress(request);
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<Product> productsPage;
            DataSourceType dataSourceType = DataSourceType.SLAVE;
            if (dataSourceType.getType().equals(type)) {
                productsPage = productReadService.findAll(pageable);
            } else {
                productsPage = productService.findAll(pageable);
            }

            if (productsPage.isEmpty()) {
                return ResponseEntity.ok(new ApiResponse<>("success", "No products found", hostname, ipAddress, null));
            }

            return ResponseEntity.ok(new ApiResponse<>("success", "Products retrieved successfully", hostname, ipAddress, productsPage));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("error", "An error occurred: " + e.getMessage(), hostname, ipAddress, null));
        }
    }

    // Get a product by ID
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Product>> getProductById(@PathVariable Long id, @RequestParam String type, HttpServletRequest request) {
        String hostname = getHostName();
        String ipAddress = getIpAddress(request);
        try {
            Optional<Product> product;
            DataSourceType dataSourceType = DataSourceType.SLAVE;
            if (dataSourceType.getType().equals(type)) {
                product = productReadService.getProductById(id);
            } else {
                product = productService.getProductById(id);
            }
            return product.map(p -> ResponseEntity.ok(new ApiResponse<>("success", "Product retrieved successfully", hostname, ipAddress, p)))
                    .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body(new ApiResponse<>("error", "Product not found", hostname, ipAddress, null)));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("error", "An error occurred: " + e.getMessage(), hostname, ipAddress, null));
        }
    }

    // Create a new product
    @PostMapping
    public ResponseEntity<ApiResponse<Product>> createProduct(@RequestBody Product product,
                                                              @RequestParam String type, HttpServletRequest request) {
        String hostname = getHostName();
        String ipAddress = getIpAddress(request);
        try {
            Product savedProduct;
            DataSourceType dataSourceType = DataSourceType.SLAVE;
            if (dataSourceType.getType().equals(type)) {
                savedProduct = productReadService.saveProduct(product);
            } else {
                savedProduct = productService.saveProduct(product);
            }
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse<>("success", "Product created successfully", hostname, ipAddress, savedProduct));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("error", "An error occurred: " + e.getMessage(), hostname, ipAddress, null));
        }
    }

    // Update a product by ID
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Product>> updateProduct(@PathVariable Long id, @RequestBody Product productDetails, @RequestParam String type, HttpServletRequest request) {
        String hostname = getHostName();
        String ipAddress = getIpAddress(request);
        try {
            Product updatedProduct;
            DataSourceType dataSourceType = DataSourceType.SLAVE;
            if (dataSourceType.getType().equals(type)) {
                updatedProduct = productReadService.updateProduct(id, productDetails);
            } else {
                updatedProduct = productService.updateProduct(id, productDetails);
            }
            return ResponseEntity.ok(new ApiResponse<>("success", "Product updated successfully", hostname, ipAddress, updatedProduct));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("error", "An error occurred: " + e.getMessage(), hostname, ipAddress, null));
        }
    }

    // Delete a product by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteProduct(@PathVariable Long id, @RequestParam String type, HttpServletRequest request) {
        String hostname = getHostName();
        String ipAddress = getIpAddress(request);
        try {
            DataSourceType dataSourceType = DataSourceType.SLAVE;
            if (dataSourceType.getType().equals(type)) {
                productReadService.deleteProduct(id);
            } else {
                productService.deleteProduct(id);
            }
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("error", "An error occurred: " + e.getMessage(), hostname, ipAddress, null));
        }
    }
}
