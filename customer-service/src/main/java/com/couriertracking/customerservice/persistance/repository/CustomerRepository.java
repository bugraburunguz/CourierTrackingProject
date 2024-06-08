package com.couriertracking.customerservice.persistance.repository;

import com.couriertracking.customerservice.persistance.entity.CustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<CustomerEntity, Long> {
}
