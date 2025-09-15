//package com.exist.HelpdeskApp.service;
//
//import com.exist.HelpdeskApp.dto.role.RoleMapper;
//import com.exist.HelpdeskApp.dto.role.RoleRequest;
//import com.exist.HelpdeskApp.dto.role.RoleResponse;
//import com.exist.HelpdeskApp.exception.businessexceptions.RoleNotFoundException;
//import com.exist.HelpdeskApp.model.Role;
//import com.exist.HelpdeskApp.repository.RoleRepository;
//import com.exist.HelpdeskApp.service.Implementations.RoleServiceImpl;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//
//@ExtendWith(MockitoExtension.class)
//public class RoleServiceImplTest {
//    @Mock
//    RoleMapper roleMapper;
//
//    @Mock
//    RoleRepository roleRepository;
//
//    @InjectMocks
//    RoleServiceImpl roleServiceImpl;
//
//    private static Role role;
//    private static RoleResponse roleResponse;
//    private static RoleRequest roleRequest;
//
//    @BeforeEach
//    void setup() {
//        role = new Role(
//                1, "Sample Role", false, 1
//        );
//        roleResponse = new RoleResponse(
//                1, "Sample Role"
//        );
//        roleRequest = new RoleRequest("Sample Role");
//    }
//
//    @Test
//    void testGetValidRoles() {
//        List<Role> roles = new ArrayList<>();
//        roles.add(role);
//        List<RoleResponse> roleResponses = new ArrayList<>();
//        roleResponses.add(roleResponse);
//        Mockito.when(roleRepository.findAll()).thenReturn(roles);
//        Mockito.when(roleMapper.toResponseList(roles)).thenReturn(roleResponses);
//
//        List<RoleResponse> result = roleServiceImpl.getRoles();
//
//        assertEquals(1, result.size());
//    }
//
//    @Test
//    void testGetListOfEmptyRoles() {
//        List<Role> roles = new ArrayList<>();
//        List<RoleResponse> roleResponses = new ArrayList<>();
//
//        Mockito.when(roleRepository.findAll()).thenReturn(roles);
//        Mockito.when(roleMapper.toResponseList(roles)).thenReturn(roleResponses);
//
//        List<RoleResponse> result = roleServiceImpl.getRoles();
//
//        assertEquals(0, result.size());
//    }
//
//    @Test
//    void testGetValidRole() {
//        Integer roleId = 1;
//        Mockito.when(roleRepository.findById(roleId)).thenReturn(Optional.of(role));
//        Mockito.when(roleMapper.toResponse(role)).thenReturn(roleResponse);
//
//        RoleResponse result = roleServiceImpl.getRole(roleId);
//
//        assertEquals("Sample Role", result.getRoleName());
//    }
//
//    @Test
//    void testGetRoleNotFound() {
//        Integer roleId = 99;
//        Mockito.when(roleRepository.findById(roleId)).thenReturn(Optional.empty());
//        assertThrows(RoleNotFoundException.class, () -> roleServiceImpl.getRole(roleId));
//    }
//
//    @Test
//    void addValidRole() {
//        Mockito.when(roleMapper.toEntity(roleRequest)).thenReturn(role);
//        Mockito.when(roleRepository.save(role)).thenReturn(role);
//        Mockito.when(roleMapper.toResponse(role)).thenReturn(roleResponse);
//
//        RoleResponse result = roleServiceImpl.addRole(roleRequest);
//        assertEquals("Sample Role", result.getRoleName());
//    }
//
//    @Test
//    void updateValidRole() {
//        Integer roleId = 1;
//        RoleRequest newRequest = new RoleRequest("New Role");
//        Role newRole = new Role(1, "New Role", false, 1);
//        RoleResponse newRoleResponse = new RoleResponse(1, "New Role");
//
//        Mockito.when(roleRepository.findById(roleId)).thenReturn(Optional.of(role));
//        Mockito.doNothing().when(roleMapper).toUpdate(newRequest, role);
//        Mockito.when(roleRepository.save(role)).thenReturn(newRole);
//        Mockito.when(roleMapper.toResponse(newRole)).thenReturn(newRoleResponse);
//
//        RoleResponse result = roleServiceImpl.updateRole(roleId, newRequest);
//
//        assertEquals("New Role", result.getRoleName());
//    }
//
//    @Test
//    void updateRoleButRoleNotFound() {
//        Integer roleId = 99;
//        Mockito.when(roleRepository.findById(roleId)).thenReturn(Optional.empty());
//        assertThrows(RoleNotFoundException.class, () -> roleServiceImpl.getRole(roleId));
//    }
//
//    @Test
//    void testDeleteValidRole() {
//        Integer roleId = 1;
//        Mockito.when(roleRepository.findById(roleId)).thenReturn(Optional.of(role));
//        Mockito.doNothing().when(roleRepository).delete(role);
//        roleServiceImpl.deleteRole(roleId);
//        Mockito.verify(roleRepository).delete(role);
//    }
//
//    @Test
//    void testDeleteRoleButRoleNotFound() {
//        Integer roleId = 99;
//        Mockito.when(roleRepository.findById(roleId)).thenReturn(Optional.empty());
//        assertThrows(RoleNotFoundException.class, () -> roleServiceImpl.deleteRole(roleId));
//    }
//}
