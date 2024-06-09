package com.couriertracking.orderservice.converter;

import com.couriertracking.ordermodel.request.OrderRequest;
import com.couriertracking.ordermodel.response.OrderResponse;
import com.couriertracking.orderservice.persistance.entity.CourierEntity;
import com.couriertracking.orderservice.persistance.entity.CustomerEntity;
import com.couriertracking.orderservice.persistance.entity.OrderEntity;
import com.couriertracking.orderservice.persistance.entity.StoreEntity;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OrderConverter {
    public static OrderEntity toOrderEntity(CustomerEntity customer, CourierEntity courier, StoreEntity store) {
        OrderEntity entity = new OrderEntity();
        entity.setCustomer(customer);
        entity.setCourier(courier);
        entity.setStore(store);
        return entity;
    }

    public static OrderResponse toOrderResponse(OrderEntity entity) {
        return OrderResponse.builder()
                .id(entity.getId())
                .customerId(entity.getCustomer().getId())
                .courierId(entity.getCourier().getId())
                .storeId(entity.getStore().getId())
                .createdDate(entity.getCreatedDate())
                .lastModifiedDate(entity.getLastModifiedDate())
                .build();
    }

    public static List<OrderResponse> toOrderResponseList(List<OrderEntity> entities) {
        return entities.stream()
                .map(OrderConverter::toOrderResponse)
                .collect(Collectors.toList());
    }
}