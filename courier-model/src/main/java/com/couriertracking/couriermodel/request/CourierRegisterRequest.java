package com.couriertracking.couriermodel.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CourierRegisterRequest {
    @NotEmpty(message = "first name cannot be empty!")
    private String firstName;
    @NotEmpty(message = "last name cannot be empty!")
    private String lastName;
}
