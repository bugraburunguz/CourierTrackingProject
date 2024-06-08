package com.couriertracking.storemodel.resonse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StoreResponse {
    private Long id;
    private String name;
    private Double lat;
    private Double lng;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;
}