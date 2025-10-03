package com.exist.HelpdeskApp.controller;

import com.exist.HelpdeskApp.dto.role.RoleFilterRequest;
import com.exist.HelpdeskApp.dto.role.RoleRequest;
import com.exist.HelpdeskApp.dto.role.RoleResponse;
import com.exist.HelpdeskApp.service.impl.RoleServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/role")
public class RoleController {

    private final RoleServiceImpl roleService;

    @Autowired
    public RoleController(RoleServiceImpl roleService) {
        this.roleService = roleService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Page<RoleResponse> getRoles(@ModelAttribute @Valid RoleFilterRequest request, Pageable pageable) {
        return roleService.getRoles(request, pageable);
    }

    @PostMapping
    public RoleResponse addRole(@RequestBody RoleRequest request) {
        return roleService.addRole(request);
    }

    @PatchMapping("/{roleId}")
    @PreAuthorize("hasRole('ADMIN')")
    public RoleResponse updateRole(@PathVariable Integer roleId, @RequestBody RoleRequest request) {
        return roleService.updateRole(roleId, request);
    }

    @DeleteMapping("/{roleId}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteRole(@PathVariable Integer roleId) {
        roleService.deleteRole(roleId);
    }
}
