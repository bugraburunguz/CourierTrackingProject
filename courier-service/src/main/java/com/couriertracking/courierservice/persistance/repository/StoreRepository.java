package com.couriertracking.courierservice.persistance.repository;

import com.couriertracking.courierservice.persistance.entity.StoreEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreRepository extends JpaRepository<StoreEntity, Long> {
}
