package com.exist.HelpdeskApp.dto.employee;

import com.exist.HelpdeskApp.model.EmploymentStatus;
import com.exist.HelpdeskApp.model.embeddable.Address;
import com.exist.HelpdeskApp.model.embeddable.Contacts;
import com.exist.HelpdeskApp.model.embeddable.Name;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeRequest {
    @Valid
    @NotNull(message = "Name must not be null")
    private Name name;

    @NotNull(message = "Age is required")
    @Min(value = 16, message = "Age must be at least 16 to be able to work")
    @Max(value = 130, message = "A person cannot be that old! That's impossible!")
    private Integer age;

    @Valid
    @NotNull(message = "Address is required")
    private Address address;

    @Valid
    @NotNull(message = "Contacts is required")
    private Contacts contacts;

    @NotNull(message = "Employment status is required")
    private EmploymentStatus employmentStatus;

    @NotNull(message = "Employee must have a role")
    private Integer roleId;
}