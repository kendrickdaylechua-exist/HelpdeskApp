package com.exist.HelpdeskApp.dto.role;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleRequest {
    private Integer id;

    @NotBlank(message = "Role name must not be blank")
    private String roleName;
}
