package com.exist.HelpdeskApp.service;

import com.exist.HelpdeskApp.dto.role.RoleMapper;
import com.exist.HelpdeskApp.dto.role.RoleRequest;
import com.exist.HelpdeskApp.dto.role.RoleResponse;
import com.exist.HelpdeskApp.exception.RoleNotFoundException;
import com.exist.HelpdeskApp.model.Role;
import com.exist.HelpdeskApp.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class RoleService {

    private final RoleMapper roleMapper;
    private final RoleRepository roleRepository;

    @Autowired
    public RoleService(RoleMapper roleMapper, RoleRepository roleRepository) {
        this.roleMapper = roleMapper;
        this.roleRepository = roleRepository;
    }

    @Transactional
    public List<RoleResponse> getRoles() {
        List<Role> roles = roleRepository.findAll();
        return roleMapper.toResponseList(roles);
    }

    @Transactional
    public RoleResponse getRole(int roleId) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new RoleNotFoundException("Role with ID " + roleId + " not found!"));
        return roleMapper.toResponse(role);
    }

    @Transactional
    public RoleResponse addRole(RoleRequest request) {
        Role role = roleMapper.toEntity(request);
        Role updated = roleRepository.save(role);
        return roleMapper.toResponse(updated);
    }

    @Transactional
    public RoleResponse updateRole(int roleId, RoleRequest request) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new RoleNotFoundException("Role with ID " + roleId + " not found!"));
        roleMapper.toUpdate(request, role);
        Role updated = roleRepository.save(role);
        return roleMapper.toResponse(updated);
    }

    @Transactional
    public void deleteRole(int roleId) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new RoleNotFoundException("Role with ID " + roleId + " not found!"));
        roleRepository.delete(role);
    }
}
