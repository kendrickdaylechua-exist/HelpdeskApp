package com.exist.HelpdeskApp.service;

import com.exist.HelpdeskApp.dto.role.RoleFilterRequest;
import com.exist.HelpdeskApp.dto.role.RoleMapper;
import com.exist.HelpdeskApp.dto.role.RoleRequest;
import com.exist.HelpdeskApp.dto.role.RoleResponse;
import com.exist.HelpdeskApp.exception.businessexceptions.EntityInUseException;
import com.exist.HelpdeskApp.exception.businessexceptions.RoleNotFoundException;
import com.exist.HelpdeskApp.model.Role;
import com.exist.HelpdeskApp.repository.EmployeeRepository;
import com.exist.HelpdeskApp.repository.RoleRepository;
import com.exist.HelpdeskApp.service.Implementations.RoleServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RoleServiceImplTest {
    @Mock
    RoleMapper roleMapper;

    @Mock
    RoleRepository roleRepository;

    @Mock
    EmployeeRepository employeeRepository;

    @InjectMocks
    RoleServiceImpl roleServiceImpl;

    private static Role role;
    private static RoleResponse roleResponse;
    private static RoleRequest roleRequest;

    @BeforeEach
    void setup() {
        role = new Role(
                1, "Sample Role", false, 1
        );
        roleResponse = new RoleResponse(
                1, "Sample Role"
        );
        roleRequest = new RoleRequest("Sample Role");
    }

    @Test
    void testGetValidRoles() {
        RoleFilterRequest request = new RoleFilterRequest();
        request.setPage(0);
        request.setSize(10);
        request.setSortBy("id");
        request.setSortDir("asc");
        request.setRoleName("Sample Role");
        request.setDeleted(false);

        Page<Role> rolePage = new PageImpl<>(List.of(role));
        when(roleRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(rolePage);
        when(roleMapper.toResponseList(List.of(role))).thenReturn(List.of(roleResponse));

        Page<RoleResponse> result = roleServiceImpl.getRoles(request);

        assertEquals(1, result.getTotalElements());
        assertEquals("Sample Role", result.getContent().get(0).getRoleName());
        verify(roleRepository).findAll(any(Specification.class), any(Pageable.class));
        verify(roleMapper).toResponseList(anyList());
    }

    @Test
    void getAllRolesDescending() {
        RoleFilterRequest request = new RoleFilterRequest();
        request.setSortDir("desc");

        Page<Role> rolePage = new PageImpl<>(List.of(role));
        when(roleRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(rolePage);
        when(roleMapper.toResponseList(List.of(role))).thenReturn(List.of(roleResponse));

        Page<RoleResponse> result = roleServiceImpl.getRoles(request);

        assertEquals(1, result.getTotalElements());
        assertEquals("Sample Role", result.getContent().get(0).getRoleName());
        verify(roleRepository).findAll(any(Specification.class), any(Pageable.class));
        verify(roleMapper).toResponseList(anyList());
    }

    @Test
    void testNoFilterGetRoles() {
        RoleFilterRequest request = new RoleFilterRequest();
        Page<Role> rolePage = new PageImpl<>(List.of(role));
        when(roleRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(rolePage);
        when(roleMapper.toResponseList(List.of(role))).thenReturn(List.of(roleResponse));

        Page<RoleResponse> result = roleServiceImpl.getRoles(request);

        assertEquals(1, result.getTotalElements());
        assertEquals("Sample Role", result.getContent().get(0).getRoleName());
        verify(roleRepository).findAll(any(Specification.class), any(Pageable.class));
        verify(roleMapper).toResponseList(anyList());
    }

    @Test
    void testNoRolesFoundInFilter() {
        RoleFilterRequest request = new RoleFilterRequest();
        request.setRoleName("Invalid");
        Page<Role> rolePage = new PageImpl<>(List.of(role));
        when(roleRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(rolePage);
        when(roleMapper.toResponseList(List.of(role))).thenReturn(List.of());

        Page<RoleResponse> result = roleServiceImpl.getRoles(request);

        assertEquals(1, result.getTotalElements());
        assertEquals(0, result.getContent().size());
        verify(roleRepository).findAll(any(Specification.class), any(Pageable.class));
        verify(roleMapper).toResponseList(anyList());
    }

    @Test
    void testGetValidRole() {
        Integer roleId = 1;
        when(roleRepository.findByIdAndDeletedFalse(roleId)).thenReturn(Optional.of(role));
        when(roleMapper.toResponse(role)).thenReturn(roleResponse);

        RoleResponse result = roleServiceImpl.getRole(roleId);

        assertEquals("Sample Role", result.getRoleName());
    }

    @Test
    void testGetRoleNotFound() {
        Integer roleId = 99;
        when(roleRepository.findByIdAndDeletedFalse(roleId)).thenReturn(Optional.empty());
        assertThrows(RoleNotFoundException.class, () -> roleServiceImpl.getRole(roleId));
    }

    @Test
    void addValidRole() {
        when(roleMapper.toEntity(roleRequest)).thenReturn(role);
        when(roleRepository.save(role)).thenReturn(role);
        when(roleMapper.toResponse(role)).thenReturn(roleResponse);

        RoleResponse result = roleServiceImpl.addRole(roleRequest);
        assertEquals("Sample Role", result.getRoleName());
    }

    @Test
    void updateValidRole() {
        Integer roleId = 1;
        RoleRequest newRequest = new RoleRequest("New Role");
        Role newRole = new Role(1, "New Role", false, 1);
        RoleResponse newRoleResponse = new RoleResponse(1, "New Role");

        when(roleRepository.findByIdAndDeletedFalse(roleId)).thenReturn(Optional.of(role));
        Mockito.doNothing().when(roleMapper).toUpdate(newRequest, role);
        when(roleRepository.save(role)).thenReturn(newRole);
        when(roleMapper.toResponse(newRole)).thenReturn(newRoleResponse);

        RoleResponse result = roleServiceImpl.updateRole(roleId, newRequest);

        assertEquals("New Role", result.getRoleName());
    }

    @Test
    void updateRoleButRoleNotFound() {
        Integer roleId = 99;
        when(roleRepository.findByIdAndDeletedFalse(roleId)).thenReturn(Optional.empty());
        assertThrows(RoleNotFoundException.class, () -> roleServiceImpl.getRole(roleId));
    }

    @Test
    void testDeleteValidRole() {
        Integer roleId = 1;
        role.setDeleted(true);
        when(roleRepository.findByIdAndDeletedFalse(roleId)).thenReturn(Optional.of(role));
        when(employeeRepository.existsByRole(role)).thenReturn(false);
        roleServiceImpl.deleteRole(roleId);
        assertTrue(role.isDeleted());
        verify(roleRepository).save(role);
    }

    @Test
    void testDeleteEmployeeButEmployeeNotFound() {
        Integer roleId = 99;
        when(roleRepository.findByIdAndDeletedFalse(roleId)).thenReturn(Optional.empty());
        assertThrows(RoleNotFoundException.class, () -> roleServiceImpl.deleteRole(roleId));
    }

    @Test
    void testDeletedEmployeeButEmployeeLinkedToTicket() {
        Integer roleId = 1;
        role.setDeleted(true);
        when(roleRepository.findByIdAndDeletedFalse(roleId)).thenReturn(Optional.of(role));
        when(employeeRepository.existsByRole(role)).thenReturn(true);
        assertThrows(EntityInUseException.class, () -> roleServiceImpl.deleteRole(roleId));
    }
}
