package com.couriertracking.orderservice.client;

import com.couriertracking.orderservice.persistance.entity.StoreEntity;
import com.couriertracking.storemodel.resonse.StoreResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "store-service", url = "${feign.client.config.store-service.url}")
public interface StoreServiceClient {

    @GetMapping("/nearest-store")
    StoreResponse findNearestStore(@RequestParam double latitude, @RequestParam double longitude);
}