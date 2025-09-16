package com.exist.HelpdeskApp.dto.embedded;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressRequest {
    @NotBlank(message = "Street cannot be blank")
    @Size(max = 100, message = "Street cannot exceed 100 characters")
    private String street;

    @NotBlank(message = "City cannot be blank")
    @Size(max = 50, message = "City cannot exceed 50 characters")
    private String city;

    @NotBlank(message = "Region cannot be blank")
    @Size(max = 50, message = "Region cannot exceed 50 characters")
    private String region;

    @NotBlank(message = "Country cannot be blank")
    @Size(max = 50, message = "Country cannot exceed 50 characters")
    private String country;
}
