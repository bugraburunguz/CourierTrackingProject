package com.couriertracking.customerservice.persistance.repository;

import com.couriertracking.customerservice.persistance.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
}
