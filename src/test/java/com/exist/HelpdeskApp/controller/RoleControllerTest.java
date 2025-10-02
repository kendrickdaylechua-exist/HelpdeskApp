package com.exist.HelpdeskApp.controller;

import com.exist.HelpdeskApp.dto.role.RoleFilterRequest;
import com.exist.HelpdeskApp.dto.role.RoleRequest;
import com.exist.HelpdeskApp.dto.role.RoleResponse;
import com.exist.HelpdeskApp.service.Implementations.RoleServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RoleController.class)
public class RoleControllerTest {

    private static RoleResponse role1;
    private static RoleRequest roleRequest1;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RoleServiceImpl roleService;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        role1 = new RoleResponse(
                2,
                "role1"
        );
        roleRequest1 = new RoleRequest(
                "role1"
        );
    }

    @Test
    void testGetAllValidRoles() throws Exception {
        Page<RoleResponse> page = new PageImpl<>(List.of(role1));
        when(roleService.getRoles(any(RoleFilterRequest.class))).thenReturn(page);
        mockMvc.perform(get("/role"))
                .andExpect(status().isOk())
                .andExpect(jsonPath(".content[0].roleName").value("role1"));
    }

    @Test
    void testGetValidRole() throws Exception {
        Integer roleId = 2;
        when(roleService.getRole(2)).thenReturn(role1);
        mockMvc.perform(get("/role/{roleId}", roleId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("roleName").value("role1"));
    }

    @Test
    void testAddValidRole() throws Exception {
        when(roleService.addRole(roleRequest1)).thenReturn(role1);
        mockMvc.perform(post("/role")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(roleRequest1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("roleName").value("role1"));
    }

    @Test
    void testUpdateValidRole() throws Exception {
        Integer roleId = 2;
        when(roleService.updateRole(roleId, roleRequest1)).thenReturn(role1);
        mockMvc.perform(
                        patch("/role/{id}", roleId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(roleRequest1))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("roleName").value("role1"));
    }

    @Test
    void testDeleteValidRole() throws Exception {
        Integer roleId = 2;
        doNothing().when(roleService).deleteRole(roleId);
        mockMvc.perform(
                        delete("/role/{id}", roleId))
                .andExpect(status().isOk());
        verify(roleService, times(1)).deleteRole(roleId);
    }
}
