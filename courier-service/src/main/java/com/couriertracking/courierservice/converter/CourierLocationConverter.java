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
           CourierLocationResponse courierLocationResponse = new CourierLocationResponse();
           courierLocationResponse.setFirstName(entity.getCourier().getFirstName());
           courierLocationResponse.setCourierStatus(entity.getCourier().getCourierStatus());
           courierLocationResponse.setLastName(entity.getCourier().getLastName());
           courierLocationResponse.setLatitude(entity.getLatitude());
           courierLocationResponse.setLongitude(entity.getLongitude());
           courierLocationResponse.setLastModifiedDate(entity.getLastModifiedDate());
        return courierLocationResponse;
    }

}
