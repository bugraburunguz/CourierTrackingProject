package com.couriertracking.courierservice.service;

import com.couriertracking.couriermodel.enums.CourierStatus;
import com.couriertracking.couriermodel.request.CourierRegisterRequest;
import com.couriertracking.couriermodel.response.CourierResponse;
import com.couriertracking.courierservice.advice.exception.CourierNotFoundException;
import com.couriertracking.courierservice.persistance.entity.CourierEntity;
import com.couriertracking.courierservice.persistance.repository.CourierLocationRepository;
import com.couriertracking.courierservice.persistance.repository.CourierRepository;
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
import static org.mockito.Mockito.*;

class CourierServiceTest {

    @Mock
    private CourierRepository courierRepository;

    @Mock
    private CourierLocationRepository courierLocationRepository;

    @InjectMocks
    private CourierService courierService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createCourier() {
        CourierRegisterRequest request = new CourierRegisterRequest();
        request.setFirstName("John");
        request.setLastName("Doe");

        courierService.createCourier(request);

        verify(courierRepository, times(1)).save(any(CourierEntity.class));
    }

    @Test
    void updateStatus() {
        CourierEntity courierEntity = new CourierEntity();
        when(courierRepository.findById(anyLong())).thenReturn(Optional.of(courierEntity));

        courierService.updateStatus(1L, CourierStatus.AVAILABLE);

        assertEquals(CourierStatus.AVAILABLE.getStatus(), courierEntity.getCourierStatus());
        verify(courierRepository, times(1)).save(courierEntity);
    }

    @Test
    void updateStatus_NotFound() {
        when(courierRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(CourierNotFoundException.class, () -> courierService.updateStatus(1L, CourierStatus.AVAILABLE));
    }

    @Test
    void getAllCouriers() {
        when(courierRepository.findAll()).thenReturn(Arrays.asList(new CourierEntity(), new CourierEntity()));

        List<CourierResponse> couriers = courierService.getAllCouriers();

        assertEquals(2, couriers.size());
    }

    @Test
    void getCourierById() {
        CourierEntity courierEntity = new CourierEntity();
        when(courierRepository.findById(anyLong())).thenReturn(Optional.of(courierEntity));

        CourierResponse response = courierService.getCourierById(1L);

        assertNotNull(response);
    }

    @Test
    void getCourierById_NotFound() {
        when(courierRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(CourierNotFoundException.class, () -> courierService.getCourierById(1L));
    }

    @Test
    void deleteCourier() {
        courierService.deleteCourier(1L);

        verify(courierRepository, times(1)).deleteById(1L);
    }
}
