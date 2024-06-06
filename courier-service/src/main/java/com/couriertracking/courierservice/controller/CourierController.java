package com.couriertracking.courierservice.controller;

import com.couriertracking.couriermodel.request.CourierRegisterRequest;
import com.couriertracking.couriermodel.request.CourierRequest;
import com.couriertracking.couriermodel.response.CourierResponse;
import com.couriertracking.courierservice.service.CourierService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/courier")
public class CourierController {

    private final CourierService courierService;

    @PostMapping
    public void createCourier(@RequestBody CourierRegisterRequest courier) {
        courierService.createCourier(courier);
    }

    @GetMapping
    public List<CourierResponse> getAllCouriers() {
        return courierService.getAllCouriers();
    }

    @GetMapping("/{id}")
    public CourierResponse getCourierById(@PathVariable Long id) {
        return courierService.getCourierById(id);
    }

    @PutMapping("/{id}")
    public void updateCourier(@PathVariable Long id, @RequestBody CourierRequest courierRequest) {
        courierService.updateCourier(id, courierRequest);
    }

    @DeleteMapping("/{id}")
    public void deleteCourier(@PathVariable Long id) {
        courierService.deleteCourier(id);
    }
}
