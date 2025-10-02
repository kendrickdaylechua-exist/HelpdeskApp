package com.exist.HelpdeskApp.service.impl;

import com.exist.HelpdeskApp.dto.role.RoleFilterRequest;
import com.exist.HelpdeskApp.dto.role.RoleMapper;
import com.exist.HelpdeskApp.dto.role.RoleRequest;
import com.exist.HelpdeskApp.dto.role.RoleResponse;
import com.exist.HelpdeskApp.exception.businessexceptions.EntityInUseException;
import com.exist.HelpdeskApp.exception.businessexceptions.RoleNotFoundException;
import com.exist.HelpdeskApp.model.Role;
import com.exist.HelpdeskApp.repository.EmployeeRepository;
import com.exist.HelpdeskApp.repository.RoleRepository;
import com.exist.HelpdeskApp.service.RoleService;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleMapper roleMapper;
    private final RoleRepository roleRepository;
    private final EmployeeRepository employeeRepository;

    private static final Logger log = LoggerFactory.getLogger(RoleServiceImpl.class);

    @Autowired
    public RoleServiceImpl(RoleMapper roleMapper, RoleRepository roleRepository, EmployeeRepository employeeRepository) {
        this.roleMapper = roleMapper;
        this.roleRepository = roleRepository;
        this.employeeRepository = employeeRepository;

    }

    @Transactional
    public Page<RoleResponse> getRoles(RoleFilterRequest request) {
        Specification<Role> spec = request.toSpec();
        Sort sort = request.getSortDir().equalsIgnoreCase("desc") ? Sort.by(request.getSortBy()).descending() : Sort.by(request.getSortBy()).ascending();
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize(), sort);
        Page<Role> rolePage = roleRepository.findAll(spec, pageable);
        List<RoleResponse> roleResponses = roleMapper.toResponseList(rolePage.getContent());
        return new PageImpl<>(roleResponses, pageable, rolePage.getTotalElements());
    }

    @Transactional
    public RoleResponse getRole(Integer roleId) {
        Role role = roleRepository.findByIdAndDeletedFalse(roleId)
                .orElseThrow(() -> new RoleNotFoundException("Role with ID " + roleId + " not found!"));
        return roleMapper.toResponse(role);
    }

    @Transactional
    public RoleResponse addRole(@Valid RoleRequest request) {
        Role role = roleMapper.toEntity(request);
        Role updated = roleRepository.save(role);
        return roleMapper.toResponse(updated);
    }

    @Transactional
    public RoleResponse updateRole(Integer roleId, RoleRequest request) {
        Role role = roleRepository.findByIdAndDeletedFalse(roleId)
                .orElseThrow(() -> new RoleNotFoundException("Role with ID " + roleId + " not found!"));
        roleMapper.toUpdate(request, role);
        Role updated = roleRepository.save(role);
        return roleMapper.toResponse(updated);
    }

    @Transactional
    public void deleteRole(Integer roleId) {
        Role role = roleRepository.findByIdAndDeletedFalse(roleId)
                .orElseThrow(() -> new RoleNotFoundException("Role with ID " + roleId + " not found!"));
        boolean isLinked = employeeRepository.existsByRole(role);
        if (isLinked) {
            log.warn("Attempt to delete {} which is linked to employee/s.", roleId);
            throw new EntityInUseException("Cannot delete Role with ID " + roleId + ". It is linked to other employee/s");
        }
        role.setDeleted(true);
        roleRepository.save(role);
    }
}
