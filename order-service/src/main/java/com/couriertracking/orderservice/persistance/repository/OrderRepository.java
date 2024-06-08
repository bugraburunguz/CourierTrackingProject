package com.couriertracking.orderservice.persistance.repository;

import com.couriertracking.orderservice.persistance.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
}
