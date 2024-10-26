package com.demo.ecommerce.respository;
import com.demo.ecommerce.data.annotations.DataSource;
import com.demo.ecommerce.data.enums.DataSourceType;
import com.demo.ecommerce.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProductReadRepository extends JpaRepository<Product, Long> {
    @Override
    @DataSource(DataSourceType.SLAVE)
    Product save(Product product);

    @Override
    @DataSource(DataSourceType.SLAVE)
    List<Product> findAll();

    @DataSource(DataSourceType.SLAVE)
    Product findById(long id);
}
