package com.demo.ecommerce.controller;

import com.demo.ecommerce.data.enums.DataSourceType;
import com.demo.ecommerce.entity.Product;
import com.demo.ecommerce.service.ProductReadService;
import com.demo.ecommerce.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
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

    // Get all products
    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts(@RequestParam String type) {
        try {
            List<Product> products = new ArrayList<Product>();
            DataSourceType dataSourceType = DataSourceType.SLAVE;
            if (dataSourceType.getType().equals(type)) {
                products.addAll(productReadService.findAll());
            } else {
                products.addAll(productService.findAll());
            }
            if (products.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(products, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Get a product by ID
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id, @RequestParam String type) {
        try {
            Optional<Product> product;
            DataSourceType dataSourceType = DataSourceType.SLAVE;
            if (dataSourceType.getType().equals(type)) {
                product = productReadService.getProductById(id);
            } else {
                product = productService.getProductById(id);
            }
            return product.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Create a new product
    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody Product product,
                                                 @RequestParam String type) {
        try {
            Product savedProduct;
            DataSourceType dataSourceType = DataSourceType.SLAVE;
            if (dataSourceType.getType().equals(type)) {
                savedProduct = productReadService.saveProduct(product);
            } else {
                savedProduct = productService.saveProduct(product);
            }
            return new ResponseEntity<>(savedProduct, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Update a product by ID
    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody Product productDetails, @RequestParam String type) {
        try {
            Product updatedProduct;
            DataSourceType dataSourceType = DataSourceType.SLAVE;
            if (dataSourceType.getType().equals(type)) {
                updatedProduct = productReadService.updateProduct(id, productDetails);
            } else {
                updatedProduct = productService.updateProduct(id, productDetails);
            }
            return ResponseEntity.ok(updatedProduct);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Delete a product by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id, @RequestParam String type) {
        try {
            DataSourceType dataSourceType = DataSourceType.SLAVE;
            if (dataSourceType.getType().equals(type)) {
                productReadService.deleteProduct(id);
            } else {
                productService.deleteProduct(id);
            }
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}