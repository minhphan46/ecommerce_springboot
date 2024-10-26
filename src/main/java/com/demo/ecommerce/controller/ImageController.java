package com.demo.ecommerce.controller;

import com.demo.ecommerce.data.enums.DataSourceType;
import com.demo.ecommerce.service.StorageReadService;
import com.demo.ecommerce.service.StorageService;
import com.demo.ecommerce.util.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("api/images")
public class ImageController {
    @Autowired
    private StorageService service;

    @Autowired
    private StorageReadService readService;

    // Test
    @GetMapping("/")
    public String test() {
        return "test upload image";
    }

    // Helper method to get host information
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

    @PostMapping("/upload")
    public ResponseEntity<ApiResponse<Long>> uploadImage(
            @RequestParam("image") MultipartFile file,
            @RequestParam String type,
            HttpServletRequest request) throws IOException {
        String hostname = getHostName();
        String ipAddress = getIpAddress(request);

        try {
            Long imageId;
            DataSourceType dataSourceType = DataSourceType.SLAVE;
            if (dataSourceType.getType().equals(type)) {
                imageId = readService.uploadImage(file);
            } else {
                imageId = service.uploadImage(file);
            }
            return ResponseEntity.ok(new ApiResponse<>("success", "Image uploaded successfully", hostname, ipAddress, imageId));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("error", "An error occurred: " + e.getMessage(), hostname, ipAddress, null));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<byte[]> downloadImage(@PathVariable Long id, @RequestParam String type) {
        try {
            byte[] imageData;
            DataSourceType dataSourceType = DataSourceType.SLAVE;
            if (dataSourceType.getType().equals(type)) {
                imageData = readService.downloadImage(id);
            } else {
                imageData = service.downloadImage(id);
            }
            return ResponseEntity.status(HttpStatus.OK)
                    .contentType(MediaType.valueOf("image/png"))
                    .body(imageData);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
