package com.couriertracking.courierservice.persistance.repository;

import com.couriertracking.courierservice.persistance.entity.CourierEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourierRepository extends JpaRepository<CourierEntity, Long> {
    List<CourierEntity> findByCourierStatus(Integer status);
}