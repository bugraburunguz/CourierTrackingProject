package com.couriertracking.courierservice.service;

import com.couriertracking.couriermodel.enums.CourierStatus;
import com.couriertracking.couriermodel.request.CourierRegisterRequest;
import com.couriertracking.couriermodel.request.CourierRequest;
import com.couriertracking.couriermodel.response.CourierResponse;
import com.couriertracking.courierservice.advice.exception.CourierNotFoundException;
import com.couriertracking.courierservice.client.EvaluationServiceClient;
import com.couriertracking.courierservice.converter.CourierConverter;
import com.couriertracking.courierservice.persistance.entity.CourierEntity;
import com.couriertracking.courierservice.persistance.entity.CourierLocationEntity;
import com.couriertracking.courierservice.persistance.repository.CourierLocationRepository;
import com.couriertracking.courierservice.persistance.repository.CourierRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

import static com.couriertracking.courierservice.converter.CourierConverter.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class CourierService {

    private final CourierRepository courierRepository;
    private final CourierLocationRepository courierLocationRepository;
    private final EvaluationServiceClient evaluationServiceClient;

    public void createCourier(CourierRegisterRequest request) {
        CourierEntity courierEntity = toCourierEntity(request);

        log.info("Created Courier: {} {}", courierEntity.getFirstName(), courierEntity.getLastName());
        courierRepository.save(courierEntity);
    }

    public List<CourierResponse> getAllCouriers() {

        return toCourierResponseList(courierRepository.findAll());
    }

    public CourierResponse getCourierById(Long id) {
        CourierEntity courierEntity = courierRepository.findById(id)
                .orElseThrow(CourierNotFoundException::new);
        return toCourierResponse(courierEntity);
    }

    @Transactional
    public CourierEntity findNearestAvailableCourier(double latitude, double longitude) {
        List<CourierEntity> availableCouriers = courierRepository.findByCourierStatus(CourierStatus.AVAILABLE.getStatus());
        CourierEntity nearestCourier = null;
        double nearestDistance = Double.MAX_VALUE;

        for (CourierEntity courier : availableCouriers) {
            CourierLocationEntity currentLocation = courierLocationRepository.findFirstByCourierOrderByCreatedDateDesc(courier);
            if (currentLocation != null) {
                double distance = evaluationServiceClient.calculateDistance(latitude, longitude, currentLocation.getLatitude(), currentLocation.getLongitude());
                if (distance < nearestDistance) {
                    nearestDistance = distance;
                    nearestCourier = courier;
                }
            }
        }

        if (nearestCourier == null) {
            throw new CourierNotFoundException();
        }

        return nearestCourier;
    }

    public double getTotalDistanceTraveled(Long courierId) {
        CourierEntity courier = courierRepository.findById(courierId)
                .orElseThrow(CourierNotFoundException::new);

        List<CourierLocationEntity> locations = courierLocationRepository.findByCourierOrderByCreatedDateAsc(courier);
        double totalDistance = 0.0;

        for (int i = 1; i < locations.size(); i++) {
            CourierLocationEntity start = locations.get(i - 1);
            CourierLocationEntity end = locations.get(i);
            totalDistance += evaluationServiceClient.calculateDistance(start.getLatitude(), start.getLongitude(), end.getLatitude(), end.getLongitude());
        }

        return totalDistance;
    }

    public void updateCourier(Long id, CourierRequest request) {
        CourierEntity courierEntity = courierRepository.findById(id)
                .orElseThrow(CourierNotFoundException::new);

        if (Objects.nonNull(request.getFirstName())) {
            courierEntity.setFirstName(request.getFirstName());
        }
        if (Objects.nonNull(request.getLastName())) {
            courierEntity.setLastName(request.getLastName());
        }
        if (Objects.nonNull(request.getCourierStatus())) {
            courierEntity.setCourierStatus(CourierStatus.of(request.getCourierStatus()));
        }
        courierRepository.save(courierEntity);
    }

    public void deleteCourier(Long id) {
        courierRepository.deleteById(id);
    }
}
