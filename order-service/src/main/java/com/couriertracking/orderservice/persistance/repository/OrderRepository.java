package com.couriertracking.orderservice.persistance.repository;

import com.couriertracking.orderservice.persistance.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
//todo tüm repositorylere @Repository ekle
public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
}
