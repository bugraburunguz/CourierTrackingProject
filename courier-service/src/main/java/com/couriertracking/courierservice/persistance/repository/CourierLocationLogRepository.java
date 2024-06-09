package com.couriertracking.courierservice.persistance.repository;

import com.couriertracking.courierservice.persistance.entity.CourierEntity;
import com.couriertracking.courierservice.persistance.entity.CourierLocationLogEntity;
import com.couriertracking.courierservice.persistance.entity.StoreEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourierLocationLogRepository extends JpaRepository<CourierLocationLogEntity, Long> {
    CourierLocationLogEntity findFirstByCourierAndStoreOrderByTimestampDesc(CourierEntity courier, StoreEntity store);
}
