package com.couriertracking.courierservice.service;

import com.couriertracking.couriermodel.enums.CourierStatus;
import com.couriertracking.couriermodel.request.CourierLocationRequest;
import com.couriertracking.couriermodel.response.CourierLocationResponse;
import com.couriertracking.courierservice.advice.exception.CourierNotFoundException;
import com.couriertracking.courierservice.client.EvaluationServiceClient;
import com.couriertracking.courierservice.persistance.entity.CourierEntity;
import com.couriertracking.courierservice.persistance.entity.CourierLocationEntity;
import com.couriertracking.courierservice.persistance.entity.CourierLocationLogEntity;
import com.couriertracking.courierservice.persistance.entity.StoreEntity;
import com.couriertracking.courierservice.persistance.repository.CourierLocationLogRepository;
import com.couriertracking.courierservice.persistance.repository.CourierLocationRepository;
import com.couriertracking.courierservice.persistance.repository.CourierRepository;
import com.couriertracking.courierservice.persistance.repository.StoreRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class CourierLocationServiceTest {

    @Mock
    private CourierLocationRepository courierLocationRepository;

    @Mock
    private CourierRepository courierRepository;

    @Mock
    private StoreRepository storeRepository;

    @Mock
    private CourierLocationLogRepository logRepository;

    @Mock
    private EvaluationServiceClient evaluationServiceClient;

    @InjectMocks
    private CourierLocationService courierLocationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void saveCurrentCourierLocation() {
        CourierLocationRequest request = new CourierLocationRequest();
        request.setCourierId(1L);
        request.setLatitude(40.0);
        request.setLongitude(29.0);

        StoreEntity store1 = new StoreEntity();
        store1.setLat(40.1);
        store1.setLng(29.1);

        StoreEntity store2 = new StoreEntity();
        store2.setLat(40.2);
        store2.setLng(29.2);

        CourierEntity courierEntity = new CourierEntity();
        when(courierRepository.findById(anyLong())).thenReturn(Optional.of(courierEntity));
        when(evaluationServiceClient.calculateDistance(store1.getLat(), store1.getLng(), request.getLatitude(), request.getLongitude())).thenReturn(0.05);
        when(storeRepository.findAll()).thenReturn(Arrays.asList(store1, store2));

        courierLocationService.saveCurrentCourierLocation(request);

        verify(courierLocationRepository, times(1)).save(any(CourierLocationEntity.class));
        verify(logRepository, times(2)).save(any(CourierLocationLogEntity.class));
    }

    @Test
    void getAllCourierLocations() {
        CourierLocationEntity courierLocationEntity1 = new CourierLocationEntity();
        CourierEntity courier = new CourierEntity();
        courierLocationEntity1.setCourier(courier);
        CourierLocationEntity courierLocationEntity2 = new CourierLocationEntity();
        courierLocationEntity2.setCourier(courier);
        when(courierLocationRepository.findAll()).thenReturn(Arrays.asList(courierLocationEntity1, courierLocationEntity2));

        List<CourierLocationResponse> locations = courierLocationService.getAllCourierLocations();

        assertEquals(2, locations.size());
    }

    @Test
    void getCourierLocationById() {
        CourierEntity courierEntity = new CourierEntity();
        courierEntity.setId(1L);

        CourierLocationEntity courierLocationEntity = new CourierLocationEntity();
        courierLocationEntity.setCourier(courierEntity);

        when(courierRepository.findById(anyLong())).thenReturn(Optional.of(courierEntity));
        when(courierLocationRepository.findFirstByCourierOrderByCreatedDateDesc(any(CourierEntity.class))).thenReturn(courierLocationEntity);

        CourierLocationResponse response = courierLocationService.getCourierLocationById(1L);

        assertNotNull(response);
    }

    @Test
    void getCourierLocationById_NotFound() {
        when(courierRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(CourierNotFoundException.class, () -> courierLocationService.getCourierLocationById(1L));
    }

    @Test
    //TODO: Bİ SOR
    void testFindNearestAvailableCourier() {
        double latitude = 40.748817;
        double longitude = -73.985428;

        CourierEntity courier1 = new CourierEntity();
        courier1.setId(1L);
        courier1.setFirstName("John");
        courier1.setLastName("Doe");
        courier1.setCourierStatus(1);

        CourierLocationEntity location1 = new CourierLocationEntity();
        location1.setCourier(courier1);
        location1.setLatitude(40.748817);
        location1.setLongitude(-73.985428);

        CourierEntity courier2 = new CourierEntity();
        courier2.setId(2L);
        courier2.setFirstName("Jane");
        courier2.setLastName("Smith");
        courier2.setCourierStatus(1);

        CourierLocationEntity location2 = new CourierLocationEntity();
        location2.setCourier(courier2);
        location2.setLatitude(40.748817);
        location2.setLongitude(-73.985428);

        when(courierRepository.findByCourierStatus(1)).thenReturn(List.of(courier1, courier2));
        when(courierLocationRepository.findFirstByCourierIdOrderByLastModifiedDateDesc(1L)).thenReturn(Optional.of(location1));
        when(courierLocationRepository.findFirstByCourierIdOrderByLastModifiedDateDesc(2L)).thenReturn(Optional.of(location2));
        when(evaluationServiceClient.calculateDistance(anyDouble(), anyDouble(), anyDouble(), anyDouble())).thenReturn(1.0);

        // Act
        CourierLocationResponse result = courierLocationService.findNearestAvailableCourier(latitude, longitude);

        // Assert
        assertNotNull(result);
        assertEquals(courier1.getId(), result.getId());
        verify(courierRepository, times(1)).findByCourierStatus(1);
        verify(courierLocationRepository, times(1)).findFirstByCourierIdOrderByLastModifiedDateDesc(1L);
        verify(courierLocationRepository, times(1)).findFirstByCourierIdOrderByLastModifiedDateDesc(2L);
        verify(evaluationServiceClient, times(2)).calculateDistance(anyDouble(), anyDouble(), anyDouble(), anyDouble());
    }

    @Test
    void testFindNearestAvailableCourier_NoAvailableCouriers() {
        // Arrange
        double latitude = 40.748817;
        double longitude = -73.985428;

        when(courierRepository.findByCourierStatus(1)).thenReturn(List.of());

        // Act & Assert
        assertThrows(CourierNotFoundException.class, () -> {
            courierLocationService.findNearestAvailableCourier(latitude, longitude);
        });

        verify(courierRepository, times(1)).findByCourierStatus(1);
    }

    @Test
    void deleteCourierLocation() {
        courierLocationService.deleteCourierLocation(1L);

        verify(courierLocationRepository, times(1)).deleteById(1L);
    }
}
