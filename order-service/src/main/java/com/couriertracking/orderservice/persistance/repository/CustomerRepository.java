package com.couriertracking.orderservice.persistance.repository;

import com.couriertracking.orderservice.persistance.entity.CustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<CustomerEntity, Long> {
}
