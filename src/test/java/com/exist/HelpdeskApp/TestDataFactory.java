package com.exist.HelpdeskApp;

import com.exist.HelpdeskApp.dto.employee.EmployeeResponse;
import com.exist.HelpdeskApp.dto.role.RoleResponse;
import com.exist.HelpdeskApp.model.EmploymentStatus;

public class TestDataFactory {
    public static EmployeeResponse admin() {
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

    public static RoleResponse adminRole() {
        return new RoleResponse(
                1,
                "Admin"
        );
    }
}
