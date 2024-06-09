package com.couriertracking.courierservice.service;

import com.couriertracking.couriermodel.enums.CourierStatus;
import com.couriertracking.couriermodel.request.CourierLocationRequest;
import com.couriertracking.couriermodel.response.CourierLocationResponse;
import com.couriertracking.couriermodel.response.CourierResponse;
import com.couriertracking.courierservice.advice.constant.ErrorCodes;
import com.couriertracking.courierservice.advice.exception.CourierNotFoundException;
import com.couriertracking.courierservice.client.EvaluationServiceClient;
import com.couriertracking.courierservice.converter.CourierConverter;
import com.couriertracking.courierservice.converter.CourierLocationConverter;
import com.couriertracking.courierservice.persistance.entity.CourierEntity;
import com.couriertracking.courierservice.persistance.entity.CourierLocationEntity;
import com.couriertracking.courierservice.persistance.entity.CourierLocationLogEntity;
import com.couriertracking.courierservice.persistance.entity.StoreEntity;
import com.couriertracking.courierservice.persistance.repository.CourierLocationLogRepository;
import com.couriertracking.courierservice.persistance.repository.CourierLocationRepository;
import com.couriertracking.courierservice.persistance.repository.CourierRepository;
import com.couriertracking.courierservice.persistance.repository.StoreRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.couriertracking.courierservice.converter.CourierConverter.toCourierResponse;
import static com.couriertracking.courierservice.converter.CourierLocationConverter.toCourierLocationEntity;

@Service
@Slf4j
@RequiredArgsConstructor
public class CourierLocationService {

    private final CourierLocationRepository courierLocationRepository;
    private final CourierRepository courierRepository;
    private final StoreRepository storeRepository;
    private final CourierLocationLogRepository logRepository;
    private final EvaluationServiceClient evaluationServiceClient;
    @Transactional
    public void saveCurrentCourierLocation(CourierLocationRequest courierLocationRequest) {
        CourierEntity courierEntity = courierRepository.findById(courierLocationRequest.getCourierId())
                .orElseThrow(CourierNotFoundException::new);
        CourierLocationEntity entity = toCourierLocationEntity(courierLocationRequest, courierEntity);

        courierLocationRepository.save(entity);
        log.info("Courier current location: {} {}", courierLocationRequest.getLatitude(), courierLocationRequest.getLongitude());

        LocalDateTime now = LocalDateTime.now();

        storeRepository.findAll().forEach(store -> {
            double distance = evaluationServiceClient.calculateDistance(
                    courierLocationRequest.getLatitude(), courierLocationRequest.getLongitude(),
                    store.getLat(), store.getLng()
            );

            if (distance < 0.1) {
                CourierLocationLogEntity lastEntry = logRepository.findFirstByCourierAndStoreOrderByTimestampDesc(courierEntity, store);

                if (lastEntry == null || ChronoUnit.MINUTES.between(lastEntry.getTimestamp(), now) > 1) {
                    CourierLocationLogEntity logEntity = new CourierLocationLogEntity();
                    logEntity.setCourier(courierEntity);
                    logEntity.setStore(store);
                    logEntity.setTimestamp(now);
                    logRepository.save(logEntity);
                }
            }
        });
    }

    public List<CourierLocationResponse> getAllCourierLocations() {
        return CourierLocationConverter.toCourierResponseList(courierLocationRepository.findAll());
    }

    public CourierLocationResponse getCourierLocationById(Long courierId) {
        CourierEntity courier = courierRepository.findById(courierId).orElseThrow(CourierNotFoundException::new);
        return CourierLocationConverter.toCourierResponse(courierLocationRepository.findFirstByCourierOrderByCreatedDateDesc(courier));
    }

    @Transactional
    public CourierLocationResponse findNearestAvailableCourier(double latitude, double longitude) {
        List<CourierEntity> availableCouriers = courierRepository.findByCourierStatus(CourierStatus.AVAILABLE.getStatus());
        if (availableCouriers.isEmpty()) {
            throw new CourierNotFoundException();
        }

        CourierEntity nearestCourier = null;
        double nearestDistance = Double.MAX_VALUE;
        for (CourierEntity courier : availableCouriers) {
            Optional<CourierLocationEntity> lastCourierLocation = courierLocationRepository.findFirstByCourierIdOrderByLastModifiedDateDesc(courier.getId());
            if (lastCourierLocation.isPresent()) {
                CourierLocationEntity currentLocation = lastCourierLocation.get();
                double distance = evaluationServiceClient.calculateDistance(latitude,
                        longitude,
                        currentLocation.getLatitude(),
                        currentLocation.getLongitude());
                if (distance < nearestDistance) {
                    nearestDistance = distance;
                    nearestCourier = courier;
                }
            }
        }

        if (nearestCourier == null) {
            throw new CourierNotFoundException();
        }
        CourierLocationEntity nearestLocation = courierLocationRepository.findFirstByCourierOrderByCreatedDateDesc(nearestCourier);
        return CourierLocationConverter.toCourierResponse(nearestLocation);
    }

    public void deleteCourierLocation(Long id) {
        courierLocationRepository.deleteById(id);
    }
}
