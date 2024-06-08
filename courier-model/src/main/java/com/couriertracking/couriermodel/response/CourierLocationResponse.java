package com.couriertracking.couriermodel.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CourierLocationResponse extends CourierResponse {
    private Double latitude;
    private Double longitude;
    private LocalDateTime lastModifiedDate;
}
