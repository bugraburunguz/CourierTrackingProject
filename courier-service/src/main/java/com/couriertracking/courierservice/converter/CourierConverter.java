package com.couriertracking.courierservice.converter;

import com.couriertracking.couriermodel.enums.CourierStatus;
import com.couriertracking.couriermodel.request.CourierRegisterRequest;
import com.couriertracking.couriermodel.response.CourierResponse;
import com.couriertracking.courierservice.persistance.entity.CourierEntity;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CourierConverter {

    public static List<CourierResponse> toCourierResponseList(List<CourierEntity> entities) {
        return entities
                .stream()
                .map(CourierConverter::toCourierResponse)
                .toList();
    }
    public static CourierResponse toCourierResponse(CourierEntity courierEntity) {
        return CourierResponse.builder()
                .id(courierEntity.getId())
                .firstName(courierEntity.getFirstName())
                .lastName(courierEntity.getLastName())
                .courierStatus(courierEntity.getCourierStatus())
                .createdDate(courierEntity.getCreatedDate())
                .lastModifiedDate(courierEntity.getLastModifiedDate())
                .build();
    }

    public static CourierEntity toCourierEntity(CourierRegisterRequest request) {
        CourierEntity entity = new CourierEntity();
        entity.setFirstName(request.getFirstName());
        entity.setLastName(request.getLastName());
        entity.setCourierStatus(CourierStatus.OFFLINE.getStatus());

        return entity;
    }
}
