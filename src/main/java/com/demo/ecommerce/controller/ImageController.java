package com.demo.ecommerce.controller;
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

    // Test
    @GetMapping("/")
    public String test(){
        return "test upload image";
    }

    @PostMapping("/upload")
    public ResponseEntity<Long> uploadImage(@RequestParam("image") MultipartFile file) throws IOException {
        Long imageId = service.uploadImage(file); // Lưu hình ảnh và lấy ID
        return ResponseEntity.status(HttpStatus.OK).body(imageId); // Trả về ID của hình ảnh
    }

    @GetMapping("/{id}")
    public ResponseEntity<byte[]> downloadImage(@PathVariable Long id) {
        byte[] imageData = service.downloadImage(id); // Tải hình ảnh theo ID
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.valueOf("image/png"))
                .body(imageData);
    }
}
