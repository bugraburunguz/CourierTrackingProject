package com.couriertracking.couriermodel.request;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourierRequest {
    private String firstName;
    private String lastName;
    private Integer courierStatus;
}
