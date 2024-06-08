package com.couriertracking.storeservice.persistance.repository;

import com.couriertracking.storeservice.persistance.entity.StoreEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreRepository extends JpaRepository<StoreEntity, Long> {
}
