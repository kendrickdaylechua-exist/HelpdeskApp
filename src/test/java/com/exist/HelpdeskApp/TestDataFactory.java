package com.exist.HelpdeskApp;

import com.exist.HelpdeskApp.dto.employee.EmployeeRequest;
import com.exist.HelpdeskApp.dto.employee.EmployeeResponse;
import com.exist.HelpdeskApp.dto.role.RoleResponse;
import com.exist.HelpdeskApp.model.Employee;
import com.exist.HelpdeskApp.model.EmploymentStatus;
import com.exist.HelpdeskApp.model.Role;

public class TestDataFactory {
    public static Employee admin() {
        return new Employee(
                1,
                "Admin",
                30,
                "N/A",
                "N/A",
                EmploymentStatus.FULL_TIME,
                adminRole(),
                1
        );
    }

    public static Role adminRole() {
        return new Role(
                1,
                "Admin",
                1
        );
    }

    public static EmployeeResponse adminEmployeeRequest() {
        return new EmployeeResponse(
                1,
                "Admin",
                30,
                "N/A",
                "N/A",
                EmploymentStatus.FULL_TIME,
                1,
                "Admin"
        );
    }
}
