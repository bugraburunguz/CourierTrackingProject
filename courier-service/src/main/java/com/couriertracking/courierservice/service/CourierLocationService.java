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
@Service
@Slf4j
@RequiredArgsConstructor
public class CourierLocationService {

    private final CourierLocationRepository courierLocationRepository;
    private final CourierRepository courierRepository;

    @Transactional
    public void saveCurrentCourierLocation(CourierLocationRequest courierLocationRequest) {
        CourierEntity courierEntity = courierRepository.findById(courierLocationRequest.getCourierId()).orElseThrow(CourierNotFoundException::new);
        CourierLocationEntity entity = CourierLocationConverter.toCourierLocationEntity(courierLocationRequest, courierEntity);

        courierLocationRepository.save(entity);
        log.info("Courier current location: {} {}", courierLocationRequest.getLatitude(), courierLocationRequest.getLongitude());
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
