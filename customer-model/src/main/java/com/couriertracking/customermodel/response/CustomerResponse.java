package com.couriertracking.customermodel.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomerResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private Double latitude;
    private Double longitude;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;
}