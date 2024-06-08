package com.couriertracking.courierservice.converter;

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
}
