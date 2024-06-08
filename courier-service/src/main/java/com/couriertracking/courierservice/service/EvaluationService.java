package com.couriertracking.courierservice.service;

import com.couriertracking.courierservice.advice.exception.CourierNotFoundException;
import com.couriertracking.courierservice.model.haversine.HaversineContext;
import com.couriertracking.courierservice.model.haversine.HaversineStrategy;
import com.couriertracking.courierservice.persistance.entity.CourierEntity;
import com.couriertracking.courierservice.persistance.entity.CourierLocationEntity;
import com.couriertracking.courierservice.persistance.repository.CourierLocationRepository;
import com.couriertracking.courierservice.persistance.repository.CourierRepository;
import com.couriertracking.courierservice.service.impl.HaversineImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class EvaluationService {
    private final CourierLocationRepository courierLocationRepository;
    private final CourierRepository courierRepository;
    public Double getTotalTravelDistance(Long courierId) {
        CourierEntity courier = courierRepository.findById(courierId).orElseThrow(CourierNotFoundException::new);
          HaversineStrategy haversineStrategy = new HaversineImpl();
          HaversineContext haversine = new HaversineContext(haversineStrategy);
        List<CourierLocationEntity> locations = courierLocationRepository.findAllByCourierOrderByLastModifiedDateAsc(courier);
        if (locations.size() < 2) {
            return 0.0;
        }

        double totalDistance = 0.0;
        for (int i = 0; i < locations.size() - 1; i++) {
            CourierLocationEntity loc1 = locations.get(i);
            CourierLocationEntity loc2 = locations.get(i + 1);
            totalDistance += haversine.executeStrategy(loc1.getLatitude(), loc1.getLongitude(), loc2.getLatitude(), loc2.getLongitude());
        }

        return totalDistance;
    }
}
