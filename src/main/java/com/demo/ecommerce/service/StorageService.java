package com.demo.ecommerce.service;

import com.demo.ecommerce.entity.ImageData;
import com.demo.ecommerce.respository.StorageRepository;
import com.demo.ecommerce.util.ImageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Service
public class StorageService {

    @Autowired
    private StorageRepository repository;

    // Upload an image and return its ID
    public Long uploadImage(MultipartFile file) throws IOException {
        ImageData imageData = repository.save(ImageData.builder()
                .name(file.getOriginalFilename())
                .type(file.getContentType())
                .imageData(ImageUtils.compressImage(file.getBytes())).build());
        return imageData.getId(); // Trả về ID của hình ảnh đã lưu
    }

    // Download an image by ID
    public byte[] downloadImage(Long id) {
        Optional<ImageData> dbImageData = repository.findById(id); // Tìm hình ảnh theo ID
        if (dbImageData.isPresent()) {
            return ImageUtils.decompressImage(dbImageData.get().getImageData());
        }
        return null; // Hoặc bạn có thể xử lý ngoại lệ nếu hình ảnh không tồn tại
    }
}