package com.couriertracking.courierservice.persistance.repository;

import com.couriertracking.courierservice.persistance.entity.CourierLocationLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourierLocationLogRepository extends JpaRepository<CourierLocationLogEntity, Long> {
}
