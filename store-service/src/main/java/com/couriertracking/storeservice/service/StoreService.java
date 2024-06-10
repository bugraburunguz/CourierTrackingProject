package com.couriertracking.storeservice.service;

import com.couriertracking.evaluationservice.factory.HaversineFactory;
import com.couriertracking.storemodel.request.StoreRequest;
import com.couriertracking.storemodel.resonse.StoreResponse;
import com.couriertracking.storeservice.advice.exception.StoreNotFoundException;
import com.couriertracking.storeservice.converter.StoreConverter;
import com.couriertracking.storeservice.persistance.repository.StoreRepository;
import com.couriertracking.storeservice.persistance.entity.StoreEntity;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StoreService {

    private final StoreRepository storeRepository;

    @Transactional
    public StoreResponse createStore(StoreRequest request) {
        StoreEntity entity = StoreConverter.toStoreEntity(request);
        StoreEntity savedEntity = storeRepository.save(entity);
        return StoreConverter.toStoreResponse(savedEntity);
    }

    public StoreResponse getStoreById(Long id) {
        StoreEntity entity = storeRepository.findById(id)
                .orElseThrow(StoreNotFoundException::new);
        return StoreConverter.toStoreResponse(entity);
    }

    public List<StoreResponse> getAllStores() {
        return StoreConverter.toStoreResponseList(storeRepository.findAll());
    }

    @Transactional
    public StoreResponse updateStore(Long id, StoreRequest request) {
        StoreEntity entity = storeRepository.findById(id)
                .orElseThrow(StoreNotFoundException::new);
        entity.setName(request.getName());
        entity.setLat(request.getLat());
        entity.setLng(request.getLng());
        StoreEntity updatedEntity = storeRepository.save(entity);
        return StoreConverter.toStoreResponse(updatedEntity);
    }

    @Transactional
    public void deleteStore(Long id) {
        storeRepository.deleteById(id);
    }

    public StoreResponse findNearestStore(double latitude, double longitude) {
        List<StoreEntity> stores = storeRepository.findAll();
        StoreEntity nearestStore = null;
        double nearestDistance = Double.MAX_VALUE;

        for (StoreEntity store : stores) {
            double distance = HaversineFactory.createHaversine().calculateDistance(latitude, longitude, store.getLat(), store.getLng());
            if (distance < nearestDistance) {
                nearestDistance = distance;
                nearestStore = store;
            }
        }

        if (nearestStore == null) {
            throw new StoreNotFoundException();
        }

        return StoreConverter.toStoreResponse(nearestStore);
    }
}