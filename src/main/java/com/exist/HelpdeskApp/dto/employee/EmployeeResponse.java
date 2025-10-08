package com.exist.HelpdeskApp.dto.employee;

import com.exist.HelpdeskApp.model.EmploymentStatus;
import com.exist.HelpdeskApp.model.embeddable.Address;
import com.exist.HelpdeskApp.model.embeddable.Contacts;
import com.exist.HelpdeskApp.model.embeddable.Name;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeResponse {
    private Integer id;
    private Name name;
    private Integer age;
    private Address address;
    private Contacts contacts;
    private EmploymentStatus employmentStatus;
    private Integer roleId;
    private String roleName;
}