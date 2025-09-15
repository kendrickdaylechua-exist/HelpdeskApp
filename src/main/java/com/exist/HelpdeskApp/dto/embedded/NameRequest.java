package com.exist.HelpdeskApp.dto.embedded;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NameRequest {
    @NotBlank(message = "First name cannot be blank")
    private String firstName;

    @NotBlank(message = "Middle name cannot be blank")
    private String middleName;

    @NotBlank(message = "Last name cannot be blank")
    private String lastName;
}