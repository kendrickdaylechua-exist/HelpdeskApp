package com.exist.HelpdeskApp.controller;

import com.exist.HelpdeskApp.dto.role.RoleFilterRequest;
import com.exist.HelpdeskApp.dto.role.RoleRequest;
import com.exist.HelpdeskApp.dto.role.RoleResponse;
import com.exist.HelpdeskApp.service.Implementations.RoleServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
    public Page<RoleResponse> getRoles(@ModelAttribute @Valid RoleFilterRequest request) {
        return roleService.getRoles(request);
    }

    @GetMapping("/{roleId}")
    public RoleResponse getRole(@PathVariable Integer roleId) {
        return roleService.getRole(roleId);
    }

    @PostMapping
    public RoleResponse addRole(@RequestBody RoleRequest request) {
        return roleService.addRole(request);
    }

    @PatchMapping("/{roleId}")
    public RoleResponse updateRole(@PathVariable Integer roleId, @RequestBody RoleRequest request) {
        return roleService.updateRole(roleId, request);
    }

    @DeleteMapping("/{roleId}")
    public void deleteRole(@PathVariable Integer roleId) {
        roleService.deleteRole(roleId);
    }
}
