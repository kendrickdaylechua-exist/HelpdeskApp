package com.exist.HelpdeskApp.dto.employee;

import com.exist.HelpdeskApp.model.EmploymentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeRequest {

    private Integer id;

    @NotBlank(message = "Name must not be blank")
    private String name;

    @NotNull(message = "Age is required")
    @Min(value = 16, message = "Age must be at least 16 to be able to work")
    private Integer age;

    @NotBlank(message = "Address must not be blank")
    private String address;

    @NotBlank(message = "Contact number must not be blank")
    private String contactNumber;

    @NotNull(message = "Employment status is required")
    private EmploymentStatus employmentStatus;

    private Integer roleId;
}