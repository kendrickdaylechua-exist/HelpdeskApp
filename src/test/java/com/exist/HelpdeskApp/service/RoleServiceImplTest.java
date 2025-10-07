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
import com.exist.HelpdeskApp.service.impl.RoleServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.AccessDeniedException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RoleServiceImplTest {
    @Mock
    private RoleMapper roleMapper;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private RoleServiceImpl roleService;

    private static Role role;
    private static RoleResponse roleResponse;
    private static RoleRequest roleRequest;

    private static Pageable pageable = PageRequest.of(0, 5);

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
        request.setRoleName("Sample Role");
        request.setDeleted(false);

        Page<Role> rolePage = new PageImpl<>(List.of(role));
        when(roleRepository.findAll(any(Specification.class), any(Pageable.class)))
                .thenReturn(rolePage);
        when(roleMapper.toResponse(any(Role.class))).thenReturn(roleResponse);

        Page<RoleResponse> result = roleService.getRoles(request, pageable);

        assertEquals(1, result.getTotalElements());
        assertEquals("Sample Role", result.getContent().get(0).getRoleName());

        verify(roleRepository).findAll(any(Specification.class), any(Pageable.class));
        verify(roleMapper).toResponse(any(Role.class));
    }


    @Test
    void getAllRolesDescending() {
        RoleFilterRequest request = new RoleFilterRequest();

        Page<Role> rolePage = new PageImpl<>(List.of(role));
        when(roleRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(rolePage);
        when(roleMapper.toResponse(any(Role.class))).thenReturn(roleResponse);

        Page<RoleResponse> result = roleService.getRoles(request, pageable);

        assertEquals(1, result.getTotalElements());
        assertEquals("Sample Role", result.getContent().get(0).getRoleName());
        verify(roleRepository).findAll(any(Specification.class), any(Pageable.class));
        verify(roleMapper).toResponse(any(Role.class));
    }

    @Test
    void testNoFilterGetRoles() {
        RoleFilterRequest request = new RoleFilterRequest();
        Page<Role> rolePage = new PageImpl<>(List.of(role));
        when(roleRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(rolePage);
        when(roleMapper.toResponse(any(Role.class))).thenReturn(roleResponse);

        Page<RoleResponse> result = roleService.getRoles(request, pageable);

        assertEquals(1, result.getTotalElements());
        assertEquals("Sample Role", result.getContent().get(0).getRoleName());
        verify(roleRepository).findAll(any(Specification.class), any(Pageable.class));
        verify(roleMapper).toResponse(any(Role.class));
    }

    @Test
    void testNoRolesFoundInFilter() {
        RoleFilterRequest request = new RoleFilterRequest();
        request.setRoleName("Invalid");

        Page<Role> emptyPage = Page.empty(pageable);
        when(roleRepository.findAll(any(Specification.class), any(Pageable.class)))
                .thenReturn(emptyPage);

        Page<RoleResponse> result = roleService.getRoles(request, pageable);

        assertEquals(0, result.getTotalElements());
        assertEquals(0, result.getContent().size());

        verify(roleRepository).findAll(any(Specification.class), any(Pageable.class));
        verify(roleMapper, never()).toResponse(any(Role.class));
    }

    @Test
    void addValidRole() {
        when(roleMapper.toEntity(roleRequest)).thenReturn(role);
        when(roleRepository.save(role)).thenReturn(role);
        when(roleMapper.toResponse(role)).thenReturn(roleResponse);

        RoleResponse result = roleService.addRole(roleRequest);
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

        RoleResponse result = roleService.updateRole(roleId, newRequest);

        assertEquals("New Role", result.getRoleName());
    }

    @Test
    void updateRoleButRoleNotFound() {
        Integer roleId = 99;
        when(roleRepository.findByIdAndDeletedFalse(roleId)).thenReturn(Optional.empty());
        assertThrows(RoleNotFoundException.class, () -> roleService.updateRole(roleId, any(RoleRequest.class)));
    }

    @Test
    void testDeleteValidRole() {
        Integer roleId = 1;
        role.setDeleted(true);
        when(roleRepository.findByIdAndDeletedFalse(roleId)).thenReturn(Optional.of(role));
        when(employeeRepository.existsByRole(role)).thenReturn(false);
        roleService.deleteRole(roleId);
        assertTrue(role.isDeleted());
        verify(roleRepository).save(role);
    }

    @Test
    void testDeleteEmployeeButEmployeeNotFound() {
        Integer roleId = 99;
        when(roleRepository.findByIdAndDeletedFalse(roleId)).thenReturn(Optional.empty());
        assertThrows(RoleNotFoundException.class, () -> roleService.deleteRole(roleId));
    }

    @Test
    void testDeletedEmployeeButEmployeeLinkedToTicket() {
        Integer roleId = 1;
        role.setDeleted(true);
        when(roleRepository.findByIdAndDeletedFalse(roleId)).thenReturn(Optional.of(role));
        when(employeeRepository.existsByRole(role)).thenReturn(true);
        assertThrows(EntityInUseException.class, () -> roleService.deleteRole(roleId));
    }

    @Test
    void testUserAttemptsToGetAllRoles() {
        RoleServiceImpl roleServiceSpy = spy(roleService);

        doThrow(new AccessDeniedException("Access is denied"))
                .when(roleServiceSpy)
                .getRoles(any(), any());

        assertThrows(AccessDeniedException.class, () -> {
            roleServiceSpy.getRoles(any(), any());
        });
    }

    @Test
    void testUserAttemptsToGetAddRole() {
        RoleServiceImpl roleServiceSpy = spy(roleService);

        doThrow(new AccessDeniedException("Access is denied"))
                .when(roleServiceSpy)
                .addRole(any());

        assertThrows(AccessDeniedException.class, () -> {
            roleServiceSpy.addRole(any());
        });
    }

    @Test
    void testUserAttemptsToUpdateRole() {
        RoleServiceImpl roleServiceSpy = spy(roleService);

        doThrow(new AccessDeniedException("Access is denied"))
                .when(roleServiceSpy)
                .updateRole(any(), any());

        assertThrows(AccessDeniedException.class, () -> {
            roleServiceSpy.updateRole(any(), any());
        });
    }

    @Test
    void testUserAttemptsToDeleteRole() {
        RoleServiceImpl roleServiceSpy = spy(roleService);

        doThrow(new AccessDeniedException("Access is denied"))
                .when(roleServiceSpy)
                .deleteRole(any());

        assertThrows(AccessDeniedException.class, () -> {
            roleServiceSpy.deleteRole(any());
        });
    }
}
