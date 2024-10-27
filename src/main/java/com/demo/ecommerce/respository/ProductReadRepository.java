package com.demo.ecommerce.respository;
import com.demo.ecommerce.data.annotations.DataSource;
import com.demo.ecommerce.data.enums.DataSourceType;
import com.demo.ecommerce.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductReadRepository extends JpaRepository<Product, Long> {
    @Override
    @DataSource(DataSourceType.SLAVE)
    Product save(Product product);

    @Override
    @DataSource(DataSourceType.SLAVE)
    Page<Product> findAll(Pageable pageable);

    @DataSource(DataSourceType.SLAVE)
    Product findById(long id);
}
