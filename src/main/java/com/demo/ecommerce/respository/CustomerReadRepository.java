package com.demo.ecommerce.respository;
import com.demo.ecommerce.data.annotations.DataSource;
import com.demo.ecommerce.data.enums.DataSourceType;
import com.demo.ecommerce.entity.Customer;
import com.demo.ecommerce.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerReadRepository extends JpaRepository<Customer, Long> {
    @Override
    @DataSource(DataSourceType.SLAVE)
    Page<Customer> findAll(Pageable pageable);
}