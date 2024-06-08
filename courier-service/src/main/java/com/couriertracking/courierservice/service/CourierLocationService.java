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

    private CourierLocationEntity toCourierLocationEntity(CourierLocationRequest courierLocationRequest, CourierEntity courierEntity) {
        CourierLocationEntity entity = new CourierLocationEntity();
        entity.setCourier(courierEntity);
        entity.setLongitude(courierLocationRequest.getLongitude());
        entity.setLatitude(courierLocationRequest.getLatitude());
        return entity;
    }
}
