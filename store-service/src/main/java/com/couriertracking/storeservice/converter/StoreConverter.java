package com.couriertracking.storeservice.converter;

import com.couriertracking.storemodel.request.StoreRequest;
import com.couriertracking.storemodel.resonse.StoreResponse;
import com.couriertracking.storeservice.persistance.entity.StoreEntity;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StoreConverter {
    public static StoreEntity toStoreEntity(StoreRequest request) {
        StoreEntity entity = new StoreEntity();
        entity.setName(request.getName());
        entity.setLat(request.getLat());
        entity.setLng(request.getLng());
        return entity;
    }

    public static StoreResponse toStoreResponse(StoreEntity entity) {
        return StoreResponse.builder()
                .id(entity.getId())
                .name(entity.getName())
                .lat(entity.getLat())
                .lng(entity.getLng())
                .createdDate(entity.getCreatedDate())
                .lastModifiedDate(entity.getLastModifiedDate())
                .build();
    }

    public static List<StoreResponse> toStoreResponseList(List<StoreEntity> entities) {
        return entities.stream()
                .map(StoreConverter::toStoreResponse)
                .collect(Collectors.toList());
    }
}
