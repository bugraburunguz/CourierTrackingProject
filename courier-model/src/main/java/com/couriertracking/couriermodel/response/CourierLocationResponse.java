package com.couriertracking.couriermodel.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourierLocationResponse {
    private Long courierId;
    private String courierName;
    private String courierSurname;
    private Double latitude;
    private Double longitude;
    private LocalDateTime lastModifiedDate;
}
