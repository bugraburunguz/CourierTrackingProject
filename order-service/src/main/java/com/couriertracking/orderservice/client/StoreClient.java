package com.couriertracking.orderservice.client;

import com.couriertracking.storemodel.resonse.StoreResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "store-service", url = "${feign.client.config.store-service.url}")
public interface StoreClient {

    @GetMapping("/api/stores/nearest")
    StoreResponse getNearestStore(@RequestParam double lat, @RequestParam double lng);
}