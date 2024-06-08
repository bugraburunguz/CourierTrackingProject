package com.couriertracking.orderservice.persistance.repository;


import com.couriertracking.orderservice.persistance.entity.CourierEntity;
import com.couriertracking.orderservice.persistance.entity.CourierLocationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourierLocationRepository extends JpaRepository<CourierLocationEntity, Long> {
    List<CourierLocationEntity> findAllByCourierOrderByLastModifiedDateAsc(CourierEntity courier);
    CourierLocationEntity findByCourierOrderByLastModifiedDateAsc(CourierEntity courier);
}
