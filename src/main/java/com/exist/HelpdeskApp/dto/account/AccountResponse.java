package com.exist.HelpdeskApp.dto.account;

import com.exist.HelpdeskApp.dto.SecurityRoleResponse;
import com.exist.HelpdeskApp.model.Employee;
import com.exist.HelpdeskApp.model.SecurityRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountResponse {
    private Integer id;
    private String username;
    private String employeeName;
    private boolean enabled;
    private Set<SecurityRoleResponse> securityRoles;
}
