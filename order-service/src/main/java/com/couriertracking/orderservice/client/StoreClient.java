package com.couriertracking.orderservice.client;

import com.couriertracking.orderservice.persistance.entity.StoreEntity;
import com.couriertracking.storemodel.resonse.StoreResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "store-service", url = "${feign.client.config.store-service.url}")
public interface StoreClient {

    @GetMapping("/stores/nearest")
    StoreEntity findNearestStore(@RequestParam Double lat, @RequestParam Double lon);
}