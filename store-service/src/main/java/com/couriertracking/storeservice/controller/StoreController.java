package com.couriertracking.storeservice.controller;

import com.couriertracking.storemodel.request.StoreRequest;
import com.couriertracking.storemodel.resonse.StoreResponse;
import com.couriertracking.storeservice.persistance.entity.StoreEntity;
import com.couriertracking.storeservice.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/stores")
@RequiredArgsConstructor
public class StoreController {

    private final StoreService storeService;

    @PostMapping
    public StoreResponse createStore(@RequestBody StoreRequest request) {
        return storeService.createStore(request);
    }

    @GetMapping("/{id}")
    public StoreResponse getStoreById(@PathVariable Long id) {
        return storeService.getStoreById(id);
    }

    @GetMapping
    public List<StoreResponse> getAllStores() {
        return storeService.getAllStores();
    }

    @PutMapping("/{id}")
    public StoreResponse updateStore(@PathVariable Long id, @RequestBody StoreRequest request) {
        return storeService.updateStore(id, request);
    }

    @GetMapping("/nearest-store")
    public StoreResponse findNearestStore(@RequestParam double latitude, @RequestParam double longitude) {
        return storeService.findNearestStore(latitude, longitude);
    }

    @DeleteMapping("/{id}")
    public void deleteStore(@PathVariable Long id) {
        storeService.deleteStore(id);
    }
}