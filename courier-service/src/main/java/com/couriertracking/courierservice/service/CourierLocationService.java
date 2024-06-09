package com.couriertracking.courierservice.service;

import com.couriertracking.couriermodel.enums.CourierStatus;
import com.couriertracking.couriermodel.request.CourierLocationRequest;
import com.couriertracking.couriermodel.response.CourierLocationResponse;
import com.couriertracking.couriermodel.response.CourierResponse;
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

    private final Map<Long, LocalDateTime> lastEntryTimes = new HashMap<>();

    @Transactional
    public void saveCurrentCourierLocation(CourierLocationRequest courierLocationRequest) {
        CourierEntity courierEntity = courierRepository.findById(courierLocationRequest.getCourierId()).orElseThrow(CourierNotFoundException::new);
        CourierLocationEntity entity = toCourierLocationEntity(courierLocationRequest, courierEntity);

        courierLocationRepository.save(entity);
        log.info("Courier current location: {} {}", courierLocationRequest.getLatitude(), courierLocationRequest.getLongitude());

        for (StoreEntity store : storeRepository.findAll()) {
            double distance = evaluationServiceClient.calculateDistance(
                    courierLocationRequest.getLatitude(), courierLocationRequest.getLongitude(),
                    store.getLat(), store.getLng()
            );

            if (distance <= 0.1) {
                LocalDateTime now = LocalDateTime.now();
                LocalDateTime lastEntryTime = lastEntryTimes.get(courierEntity.getId());

                if (lastEntryTime == null || ChronoUnit.MINUTES.between(lastEntryTime, now) > 1) {
                    lastEntryTimes.put(courierEntity.getId(), now);
                    CourierLocationLogEntity logEntity = new CourierLocationLogEntity();
                    logEntity.setCourier(courierEntity);
                    logEntity.setStore(store);
                    logEntity.setTimestamp(now);
                    logRepository.save(logEntity);
                }
            }
        }
    }

    public List<CourierLocationResponse> getAllCourierLocations() {
        return CourierLocationConverter.toCourierResponseList(courierLocationRepository.findAll());
    }

    public CourierLocationResponse getCourierLocationById(Long courierId) {
        CourierEntity courier = courierRepository.findById(courierId).orElseThrow(CourierNotFoundException::new);
        return CourierLocationConverter.toCourierResponse(courierLocationRepository.findByCourierOrderByLastModifiedDateAsc(courier));
    }

    public void deleteCourierLocation(Long id) {
        courierLocationRepository.deleteById(id);
    }
}
