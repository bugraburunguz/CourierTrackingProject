package com.couriertracking.courierservice.service;

import com.couriertracking.couriermodel.request.CourierLocationRequest;
import com.couriertracking.couriermodel.response.CourierLocationResponse;
import com.couriertracking.couriermodel.response.CourierResponse;
import com.couriertracking.courierservice.advice.exception.CourierNotFoundException;
import com.couriertracking.courierservice.converter.CourierLocationConverter;
import com.couriertracking.courierservice.persistance.entity.CourierEntity;
import com.couriertracking.courierservice.persistance.entity.CourierLocationEntity;
import com.couriertracking.courierservice.persistance.repository.CourierLocationRepository;
import com.couriertracking.courierservice.persistance.repository.CourierRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.couriertracking.courierservice.advice.constant.CourierServiceConstant.EARTH_RADIUS;
import static com.couriertracking.courierservice.converter.CourierConverter.toCourierResponseList;

@Service
@Slf4j
@RequiredArgsConstructor
public class CourierLocationService {

    private final CourierLocationRepository courierLocationRepository;
    private final CourierRepository courierRepository;

    @Transactional
    public void saveCurrentCourierLocation(CourierLocationRequest courierLocationRequest) {
        CourierEntity courierEntity = courierRepository.findById(courierLocationRequest.getCourierId()).orElseThrow(CourierNotFoundException::new);
        CourierLocationEntity entity = toCourierLocationEntity(courierLocationRequest, courierEntity);

        courierLocationRepository.save(entity);
        log.info("Courier current location: {} {}", courierLocationRequest.getLatitude(), courierLocationRequest.getLongitude());
        //TODO: Kafka listener eklenecek
    }

    public List<CourierLocationResponse> getAllCourierLocations() {
        return CourierLocationConverter.toCourierResponseList(courierLocationRepository.findAll());
    }

    public CourierLocationResponse getCourierLocationById(Long id) {
        return CourierLocationConverter.toCourierResponse(courierLocationRepository.findById(id).orElseThrow(CourierNotFoundException::new));
    }

    public CourierLocationEntity updateCourierLocation(Long id, CourierLocationEntity locationDetails) {
        Optional<CourierLocationEntity> optionalLocation = courierLocationRepository.findById(id);
        if (optionalLocation.isPresent()) {
            CourierLocationEntity location = optionalLocation.get();
            location.setLatitude(locationDetails.getLatitude());
            location.setLongitude(locationDetails.getLongitude());
            location.setLastModifiedDate(locationDetails.getLastModifiedDate());
            return courierLocationRepository.save(location);
        } else {
            throw new RuntimeException("Courier location not found with id " + id);
        }
    }

    // Delete
    public void deleteCourierLocation(Long id) {
        courierLocationRepository.deleteById(id);
    }

    public Double getTotalTravelDistance(Long courierId) {
        List<CourierLocationEntity> locations = courierLocationRepository.findByCourierIdOrderByLastModifiedDateAsc(courierId);
        if (locations.size() < 2) {
            return 0.0;
        }

        double totalDistance = 0.0;
        for (int i = 0; i < locations.size() - 1; i++) {
            CourierLocationEntity loc1 = locations.get(i);
            CourierLocationEntity loc2 = locations.get(i + 1);
            totalDistance += calculateDistance(loc1.getLatitude(), loc1.getLongitude(), loc2.getLatitude(), loc2.getLongitude());
        }

        return totalDistance;
    }

    double haversine(double val) {
        return Math.pow(Math.sin(val / 2), 2);
    }

    private double calculateDistance(double startLat, double startLong, double endLat, double endLong) {
        double dLat = Math.toRadians((endLat - startLat));
        double dLong = Math.toRadians((endLong - startLong));

        startLat = Math.toRadians(startLat);
        endLat = Math.toRadians(endLat);

        double a = haversine(dLat) + Math.cos(startLat) * Math.cos(endLat) * haversine(dLong);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return EARTH_RADIUS * c;
    }

    private CourierLocationEntity toCourierLocationEntity(CourierLocationRequest courierLocationRequest, CourierEntity courierEntity) {
        CourierLocationEntity entity = new CourierLocationEntity();
        entity.setCourier(courierEntity);
        entity.setLongitude(courierLocationRequest.getLongitude());
        entity.setLatitude(courierLocationRequest.getLatitude());
        return entity;
    }
}
