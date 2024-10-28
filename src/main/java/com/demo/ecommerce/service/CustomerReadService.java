package com.demo.ecommerce.service;

import com.demo.ecommerce.entity.Customer;
import com.demo.ecommerce.respository.CustomerReadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class CustomerReadService {
    @Autowired
    private CustomerReadRepository customerRepository;

    // Get all customers
    public Page<Customer> findAll(Pageable pageable) {
        return customerRepository.findAll(pageable);
    }
}