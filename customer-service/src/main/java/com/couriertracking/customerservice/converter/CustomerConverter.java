package com.couriertracking.customerservice.converter;

import com.couriertracking.customermodel.request.CustomerRequest;
import com.couriertracking.customermodel.response.CustomerResponse;
import com.couriertracking.customerservice.persistance.entity.CustomerEntity;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CustomerConverter {

    public static CustomerEntity toCustomerEntity(CustomerRequest request) {
        CustomerEntity entity = new CustomerEntity();
        entity.setFirstName(request.getFirstName());
        entity.setLastName(request.getLastName());
        entity.setLatitude(request.getLatitude());
        entity.setLongitude(request.getLongitude());
        return entity;
    }

    public static CustomerResponse toCustomerResponse(CustomerEntity entity) {
        return CustomerResponse.builder()
                .id(entity.getId())
                .firstName(entity.getFirstName())
                .lastName(entity.getLastName())
                .latitude(entity.getLatitude())
                .longitude(entity.getLongitude())
                .createdDate(entity.getCreatedDate())
                .lastModifiedDate(entity.getLastModifiedDate())
                .build();
    }

    public static List<CustomerResponse> toCustomerResponseList(List<CustomerEntity> entities) {
        return entities.stream()
                .map(CustomerConverter::toCustomerResponse)
                .collect(Collectors.toList());
    }
}

