package com.couriertracking.courierservice.service;

import com.couriertracking.couriermodel.enums.CourierStatus;
import com.couriertracking.couriermodel.request.CourierRegisterRequest;
import com.couriertracking.couriermodel.request.CourierRequest;
import com.couriertracking.couriermodel.response.CourierResponse;
import com.couriertracking.courierservice.advice.exception.CourierNotFoundException;
import com.couriertracking.courierservice.persistance.entity.CourierEntity;
import com.couriertracking.courierservice.persistance.repository.CourierRepository;
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

    private CourierEntity toCourierEntity(CourierRegisterRequest request) {
        CourierEntity entity = new CourierEntity();
        entity.setFirstName(request.getFirstName());
        entity.setLastName(request.getLastName());
        entity.setCourierStatus(CourierStatus.OFFLINE.getStatus());

        return entity;
    }

}
