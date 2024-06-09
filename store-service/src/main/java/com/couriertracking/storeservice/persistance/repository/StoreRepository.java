package com.couriertracking.storeservice.persistance.repository;

import com.couriertracking.storeservice.persistance.entity.StoreEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StoreRepository extends JpaRepository<StoreEntity, Long> {
}
