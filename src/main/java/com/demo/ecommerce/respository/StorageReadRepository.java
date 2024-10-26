package com.demo.ecommerce.respository;

import com.demo.ecommerce.data.annotations.DataSource;
import com.demo.ecommerce.data.enums.DataSourceType;
import com.demo.ecommerce.entity.ImageData;
import com.demo.ecommerce.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StorageReadRepository extends JpaRepository<ImageData,Long> {
    @Override
    @DataSource(DataSourceType.SLAVE)
    ImageData save(ImageData imageData);

    @Override
    @DataSource(DataSourceType.SLAVE)
    List<ImageData> findAll();

    @Override
    @DataSource(DataSourceType.SLAVE)
    Optional<ImageData> findById(Long id);
    
    @DataSource(DataSourceType.SLAVE)
    Optional<ImageData> findByName(String fileName);
}