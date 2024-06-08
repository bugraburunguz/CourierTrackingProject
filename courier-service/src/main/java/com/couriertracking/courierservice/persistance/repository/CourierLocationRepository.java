package com.couriertracking.courierservice.persistance.repository;

import com.couriertracking.courierservice.persistance.entity.CourierEntity;
import com.couriertracking.courierservice.persistance.entity.CourierLocationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourierLocationRepository extends JpaRepository<CourierLocationEntity, Long> {
    List<CourierLocationEntity> findAllByCourierOrderByLastModifiedDateAsc(CourierEntity courier);
    CourierLocationEntity findByCourierOrderByLastModifiedDateAsc(CourierEntity courier);


}
