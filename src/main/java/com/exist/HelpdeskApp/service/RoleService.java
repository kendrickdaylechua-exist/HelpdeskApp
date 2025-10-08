package com.exist.HelpdeskApp.service;

import com.exist.HelpdeskApp.dto.role.RoleFilterRequest;
import com.exist.HelpdeskApp.dto.role.RoleRequest;
import com.exist.HelpdeskApp.dto.role.RoleResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.validation.Valid;
import java.util.List;

public interface RoleService {
    Page<RoleResponse> getRoles(RoleFilterRequest request, Pageable pageable);
    RoleResponse addRole(@Valid RoleRequest request);
    RoleResponse updateRole(Integer roleId, RoleRequest request);
    void deleteRole(Integer roleId);
}
