package com.couriertracking.courierservice.persistance.repository;

import com.couriertracking.courierservice.persistance.entity.CourierEntity;
import com.couriertracking.courierservice.persistance.entity.CourierLocationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourierLocationRepository extends JpaRepository<CourierLocationEntity, Long> {
    Optional<CourierLocationEntity> findFirstByCourierIdOrderByLastModifiedDateDesc(Long courierId);
    CourierLocationEntity findFirstByCourierOrderByCreatedDateDesc(CourierEntity courier);
    List<CourierLocationEntity> findByCourierOrderByCreatedDateAsc(CourierEntity courier);
}
