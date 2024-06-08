package com.couriertracking.courierservice.controller;

import com.couriertracking.couriermodel.request.CourierLocationRequest;
import com.couriertracking.couriermodel.response.CourierLocationResponse;
import com.couriertracking.courierservice.service.CourierLocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/courier/location")
public class CourierLocationController {

    private final CourierLocationService courierLocationService;

    @PostMapping
    public ResponseEntity<Void> saveCurrentCourierLocation(@RequestBody CourierLocationRequest courierLocationRequest) {
        courierLocationService.saveCurrentCourierLocation(courierLocationRequest);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<CourierLocationResponse>> getAllCourierLocations() {
        List<CourierLocationResponse> responses = courierLocationService.getAllCourierLocations();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{courierId}")
    public ResponseEntity<CourierLocationResponse> getCourierLocationById(@PathVariable Long courierId) {
        CourierLocationResponse response = courierLocationService.getCourierLocationById(courierId);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourierLocation(@PathVariable Long id) {
        courierLocationService.deleteCourierLocation(id);
        return ResponseEntity.noContent().build();
    }
}