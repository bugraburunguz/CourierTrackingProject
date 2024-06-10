package com.couriertracking.courierservice.service;

import com.couriertracking.couriermodel.request.CourierLocationRequest;
import com.couriertracking.couriermodel.response.CourierLocationResponse;
import com.couriertracking.courierservice.advice.exception.CourierNotFoundException;
import com.couriertracking.courierservice.persistance.entity.CourierEntity;
import com.couriertracking.courierservice.persistance.entity.CourierLocationEntity;
import com.couriertracking.courierservice.persistance.entity.CourierLocationLogEntity;
import com.couriertracking.courierservice.persistance.entity.StoreEntity;
import com.couriertracking.courierservice.persistance.repository.CourierLocationLogRepository;
import com.couriertracking.courierservice.persistance.repository.CourierLocationRepository;
import com.couriertracking.courierservice.persistance.repository.CourierRepository;
import com.couriertracking.courierservice.persistance.repository.StoreRepository;
import com.couriertracking.evaluationservice.factory.HaversineFactory;
import com.couriertracking.evaluationservice.model.Haversine;
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
    private Haversine haversine = HaversineFactory.createHaversine();

    @InjectMocks
    private CourierLocationService courierLocationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
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
