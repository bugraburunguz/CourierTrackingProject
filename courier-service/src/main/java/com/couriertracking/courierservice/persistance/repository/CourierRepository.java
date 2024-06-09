package com.couriertracking.courierservice.persistance.repository;

import com.couriertracking.courierservice.persistance.entity.CourierEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourierRepository extends JpaRepository<CourierEntity, Long> {
    List<CourierEntity> findByCourierStatus(Integer status);

}