package com.exist.HelpdeskApp.dto.account;

import com.exist.HelpdeskApp.model.SecurityRole;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Set;

@Data
public class AccountRequest {
    @NotNull(message = "Username cannot be empty")
    private String username;

    @NotNull(message = "Password cannot be empty")
    private String password;

    private Integer employeeId;

    @NotNull(message = "Account must have at least a role")
    private Set<SecurityRole> securityRoles;
}
