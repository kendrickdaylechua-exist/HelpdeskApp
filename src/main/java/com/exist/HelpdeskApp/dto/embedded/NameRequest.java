package com.exist.HelpdeskApp.dto.embedded;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NameRequest {
    @NotBlank(message = "First name cannot be blank")
    @Size(max = 30, message = "First name cannot exceeds 30 characters")
    private String firstName;

    @NotBlank(message = "Middle name cannot be blank")
    @Size(max = 30, message = "Middle name cannot exceeds 30 characters")
    private String middleName;

    @NotBlank(message = "Last name cannot be blank")
    @Size(max = 30, message = "Last name cannot exceeds 30 characters")
    private String lastName;
}