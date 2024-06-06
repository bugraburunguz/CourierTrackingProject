package com.couriertracking.couriermodel.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CourierLocationRequest {
    private Long courierId;
    private Double latitude;
    private Double longitude;
}
