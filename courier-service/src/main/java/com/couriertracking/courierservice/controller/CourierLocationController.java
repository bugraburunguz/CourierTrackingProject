package com.couriertracking.courierservice.controller;

import com.couriertracking.couriermodel.request.CourierLocationRequest;
import com.couriertracking.couriermodel.response.CourierLocationResponse;
import com.couriertracking.courierservice.persistance.entity.CourierEntity;
import com.couriertracking.courierservice.service.CourierLocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/courier-locations")
//TODO: PATH DEĞİŞTİ !!!
public class CourierLocationController {

    private final CourierLocationService courierLocationService;

    @PostMapping
    public ResponseEntity<Void> saveCurrentCourierLocation(@RequestBody CourierLocationRequest courierLocationRequest) {
        courierLocationService.saveCurrentCourierLocation(courierLocationRequest);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public List<CourierLocationResponse> getAllCourierLocations() {
        return courierLocationService.getAllCourierLocations();
    }

    @GetMapping("/{courierId}")
    public CourierLocationResponse getCourierLocationById(@PathVariable Long courierId) {
        return courierLocationService.getCourierLocationById(courierId);
    }

    @DeleteMapping("/{id}")
    public void deleteCourierLocation(@PathVariable Long id) {
        courierLocationService.deleteCourierLocation(id);
    }

    @GetMapping("/nearest-courier")
    public CourierLocationResponse findNearestCourier(@RequestParam double latitude, @RequestParam double longitude) {
        return courierLocationService.findNearestAvailableCourier(latitude, longitude);
    }
}