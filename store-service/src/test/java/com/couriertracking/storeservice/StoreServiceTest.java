package com.couriertracking.storeservice;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.couriertracking.storemodel.request.StoreRequest;
import com.couriertracking.storemodel.resonse.StoreResponse;
import com.couriertracking.storeservice.advice.exception.StoreNotFoundException;
import com.couriertracking.storeservice.client.EvaluationServiceClient;
import com.couriertracking.storeservice.converter.StoreConverter;
import com.couriertracking.storeservice.persistance.entity.StoreEntity;
import com.couriertracking.storeservice.persistance.repository.StoreRepository;
import com.couriertracking.storeservice.service.StoreService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

class StoreServiceTest {

    @Mock
    private StoreRepository storeRepository;

    @Mock
    private EvaluationServiceClient evaluationServiceClient;

    @InjectMocks
    private StoreService storeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createStore() {
        StoreRequest request = new StoreRequest("Store1", 40.748817, -73.985428);
        StoreEntity entity = StoreConverter.toStoreEntity(request);
        StoreEntity savedEntity = new StoreEntity();
        savedEntity.setId(1L);
        savedEntity.setName(request.getName());
        savedEntity.setLat(request.getLat());
        savedEntity.setLng(request.getLng());

        when(storeRepository.save(any(StoreEntity.class))).thenReturn(savedEntity);

        StoreResponse response = storeService.createStore(request);

        assertNotNull(response);
        assertEquals(savedEntity.getId(), response.getId());
        assertEquals(savedEntity.getName(), response.getName());
        verify(storeRepository, times(1)).save(any(StoreEntity.class));
    }

    @Test
    void getStoreById() {
        StoreEntity entity = new StoreEntity();
        entity.setId(1L);
        entity.setName("Store1");
        entity.setLat(40.748817);
        entity.setLng(-73.985428);

        when(storeRepository.findById(1L)).thenReturn(Optional.of(entity));

        StoreResponse response = storeService.getStoreById(1L);

        assertNotNull(response);
        assertEquals(entity.getId(), response.getId());
        assertEquals(entity.getName(), response.getName());
        verify(storeRepository, times(1)).findById(1L);
    }

    @Test
    void getStoreById_NotFound() {
        when(storeRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(StoreNotFoundException.class, () -> storeService.getStoreById(1L));

        verify(storeRepository, times(1)).findById(1L);
    }

    @Test
    void getAllStores() {
        StoreEntity store1 = new StoreEntity();
        store1.setId(1L);
        store1.setName("Store1");
        store1.setLat(40.748817);
        store1.setLng(-73.985428);

        StoreEntity store2 = new StoreEntity();
        store2.setId(2L);
        store2.setName("Store2");
        store2.setLat(41.902783);
        store2.setLng(12.496366);

        when(storeRepository.findAll()).thenReturn(List.of(store1, store2));

        List<StoreResponse> responses = storeService.getAllStores();

        assertNotNull(responses);
        assertEquals(2, responses.size());
        verify(storeRepository, times(1)).findAll();
    }

    @Test
    void updateStore() {
        StoreRequest request = new StoreRequest("UpdatedStore", 41.902783, 12.496366);
        StoreEntity entity = new StoreEntity();
        entity.setId(1L);
        entity.setName("Store1");
        entity.setLat(40.748817);
        entity.setLng(-73.985428);

        when(storeRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(storeRepository.save(any(StoreEntity.class))).thenReturn(entity);

        StoreResponse response = storeService.updateStore(1L, request);

        assertNotNull(response);
        assertEquals(entity.getId(), response.getId());
        assertEquals(request.getName(), response.getName());
        verify(storeRepository, times(1)).findById(1L);
        verify(storeRepository, times(1)).save(any(StoreEntity.class));
    }

    @Test
    void updateStore_NotFound() {
        StoreRequest request = new StoreRequest("UpdatedStore", 41.902783, 12.496366);

        when(storeRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(StoreNotFoundException.class, () -> storeService.updateStore(1L, request));

        verify(storeRepository, times(1)).findById(1L);
    }

    @Test
    void deleteStore() {
        Long storeId = 1L;

        storeService.deleteStore(storeId);

        verify(storeRepository, times(1)).deleteById(storeId);
    }

    @Test
    void findNearestStore() {
        StoreEntity store1 = new StoreEntity();
        store1.setId(1L);
        store1.setName("Store1");
        store1.setLat(40.748817);
        store1.setLng(-73.985428);

        StoreEntity store2 = new StoreEntity();
        store2.setId(2L);
        store2.setName("Store2");
        store2.setLat(41.902783);
        store2.setLng(12.496366);

        when(storeRepository.findAll()).thenReturn(List.of(store1, store2));
        when(evaluationServiceClient.calculateDistance(anyDouble(), anyDouble(), eq(40.748817), eq(-73.985428))).thenReturn(10.0);
        when(evaluationServiceClient.calculateDistance(anyDouble(), anyDouble(), eq(41.902783), eq(12.496366))).thenReturn(5.0);

        StoreResponse response = storeService.findNearestStore(40.748817, -73.985428);

        assertNotNull(response);
        assertEquals(store2.getId(), response.getId());
        verify(storeRepository, times(1)).findAll();
        verify(evaluationServiceClient, times(2)).calculateDistance(anyDouble(), anyDouble(), anyDouble(), anyDouble());
    }

    @Test
    void findNearestStore_NotFound() {
        when(storeRepository.findAll()).thenReturn(List.of());

        assertThrows(StoreNotFoundException.class, () -> storeService.findNearestStore(40.748817, -73.985428));

        verify(storeRepository, times(1)).findAll();
    }
}
