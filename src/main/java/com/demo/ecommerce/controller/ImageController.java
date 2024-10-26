package com.demo.ecommerce.controller;
import com.demo.ecommerce.data.enums.DataSourceType;
import com.demo.ecommerce.entity.Product;
import com.demo.ecommerce.service.StorageReadService;
import com.demo.ecommerce.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("api/images")
public class ImageController {
    @Autowired
    private StorageService service;

    @Autowired
    private StorageReadService readService;

    // Test
    @GetMapping("/")
    public String test(){
        return "test upload image";
    }

    @PostMapping("/upload")
    public ResponseEntity<Long> uploadImage(@RequestParam("image") MultipartFile file, @RequestParam String type) throws IOException {
        try {
            Long imageId;
            DataSourceType dataSourceType = DataSourceType.SLAVE;
            if (dataSourceType.getType().equals(type)) {
                imageId = readService.uploadImage(file);
            } else {
                imageId = service.uploadImage(file);
            }
            return ResponseEntity.status(HttpStatus.OK).body(imageId);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
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
