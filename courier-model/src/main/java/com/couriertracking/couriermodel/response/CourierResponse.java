package com.couriertracking.couriermodel.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourierResponse {
    private String firstName;
    private String lastName;
    private Integer courierStatus;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;
}
