package com.couriertracking.courierservice.persistance.repository;

import com.couriertracking.courierservice.persistance.entity.StoreEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface StoreRepository extends JpaRepository<StoreEntity, Long> {
}
