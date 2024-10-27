package com.demo.ecommerce.controller;

import com.demo.ecommerce.data.enums.DataSourceType;
import com.demo.ecommerce.entity.Customer;
import com.demo.ecommerce.entity.Product;
import com.demo.ecommerce.service.CustomerService;
import com.demo.ecommerce.util.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/customer")
public class CustomerController {
    @Autowired
    private CustomerService customerService;

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
    public ResponseEntity<ApiResponse<Page<Customer>>> getAllCustomers(
            HttpServletRequest request,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        String hostname = getHostName();
        String ipAddress = getIpAddress(request);
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<Customer> customerPage;
            
            customerPage = customerService.findAll(pageable);

            if (customerPage.isEmpty()) {
                return ResponseEntity.ok(new ApiResponse<>("success", "No customers found", hostname, ipAddress, null));
            }

            return ResponseEntity.ok(new ApiResponse<>("success", "Customers retrieved successfully", hostname, ipAddress, customerPage));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("error", "An error occurred: " + e.getMessage(), hostname, ipAddress, null));
        }
    }
}
