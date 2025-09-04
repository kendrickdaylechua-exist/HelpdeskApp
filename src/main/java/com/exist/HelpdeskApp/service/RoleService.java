package com.exist.HelpdeskApp.service;

import com.exist.HelpdeskApp.dto.role.RoleMapper;
import com.exist.HelpdeskApp.dto.role.RoleRequest;
import com.exist.HelpdeskApp.dto.role.RoleResponse;
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
//        Role noRole = new Role(1, "No Role", 0);
//        roleRepository.save(noRole);
    }

    @Transactional
    public List<RoleResponse> getRoles() {
        List<Role> roles = roleRepository.findAll();
        return roleMapper.toResponseList(roles);
    }

    @Transactional
    public RoleResponse getRole(int id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ID not found!"));
        return roleMapper.toResponse(role);
    }

    @Transactional
    public void addRoles(RoleRequest request) {
        Role role = roleMapper.toEntity(request);
        roleRepository.save(role);
    }

    @Transactional
    public void updateRole(int id, RoleRequest request) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ID not found!"));
        roleMapper.toUpdate(request, role);
    }

    @Transactional
    public void deleteRole(int id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ID not found!"));
        roleRepository.delete(role);
    }
}
