package com.couriertracking.courierservice.converter;

import com.couriertracking.couriermodel.response.CourierLocationResponse;
import com.couriertracking.couriermodel.response.CourierResponse;
import com.couriertracking.courierservice.persistance.entity.CourierEntity;
import com.couriertracking.courierservice.persistance.entity.CourierLocationEntity;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CourierLocationConverter {

       public static List<CourierLocationResponse> toCourierResponseList(List<CourierLocationEntity> entities) {
        return entities
                .stream()
                .map(CourierLocationConverter::toCourierResponse)
                .toList();
    }

 public static CourierLocationResponse toCourierResponse(CourierLocationEntity entity) {
        return CourierLocationResponse.builder()
                .courierId(entity.getCourier().getId())
                .courierName(entity.getCourier().getFirstName())
                .courierSurname(entity.getCourier().getLastName())
                .latitude(entity.getLatitude())
                .longitude(entity.getLongitude())
                .lastModifiedDate(entity.getLastModifiedDate())
                .build();
    }

}
