package com.couriertracking.orderservice.persistance.repository;

import com.couriertracking.orderservice.persistance.entity.CourierEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourierRepository extends JpaRepository<CourierEntity, Long> { }