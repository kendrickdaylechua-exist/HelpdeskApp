package com.exist.HelpdeskApp.dto.employee;

import com.exist.HelpdeskApp.model.EmploymentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeResponse {
    private String name;
    private Integer age;
    private String address;
    private String contactNumber;
    private EmploymentStatus employmentStatus;
}
