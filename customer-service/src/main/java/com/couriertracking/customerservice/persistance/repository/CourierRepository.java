package com.couriertracking.customerservice.persistance.repository;

import com.couriertracking.customerservice.persistance.entity.CourierEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourierRepository extends JpaRepository<CourierEntity, Long> {

}