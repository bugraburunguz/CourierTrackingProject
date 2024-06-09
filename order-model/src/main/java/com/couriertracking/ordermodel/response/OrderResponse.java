package com.couriertracking.ordermodel.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponse {
    private Long id;
    private Long customerId;
    private Long courierId;
    private Long storeId;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;
}