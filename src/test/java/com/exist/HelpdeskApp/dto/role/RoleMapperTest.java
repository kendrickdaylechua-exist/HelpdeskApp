package com.exist.HelpdeskApp.dto.role;

import com.exist.HelpdeskApp.model.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RoleMapperTest {

    private RoleMapper mapper;

    @BeforeEach
    void setup() {
        mapper = Mappers.getMapper(RoleMapper.class);
    }

    @Test
    void testToEntity() {
        RoleRequest request = new RoleRequest(
                "Test Role"
        );
        Role entity = mapper.toEntity(request);
        assertEquals("Test Role", request.getRoleName());
    }

    @Test
    void testToResponse() {
        Role role = new Role(
                1,
                "Test Role",
                false,
                1
        );
        RoleResponse response = mapper.toResponse(role);
        assertEquals(1, response.getId());
        assertEquals("Test Role", response.getRoleName());
    }

    @Test
    void testToUpdate() {
        RoleRequest request = new RoleRequest(
                "New Role Name"
        );
        Role entity = new Role(
                1,
                "Old Name",
                false,
                1
        );
        mapper.toUpdate(request, entity);
        assertEquals("New Role Name", request.getRoleName());
    }

    @Test
    void testToResponseList() {
        Role role1 = new Role(
                1,
                "Test Role 1",
                false,
                1
        );
        Role role2 = new Role(
                1,
                "Test Role 2",
                false,
                1
        );
        List<Role> roleList = new ArrayList<>();
        roleList.add(role1);
        roleList.add(role2);

        List<RoleResponse> roleResponsesList = mapper.toResponseList(roleList);

        assertEquals("Test Role 1", roleResponsesList.get(0).getRoleName());
        assertEquals("Test Role 2", roleResponsesList.get(1).getRoleName());
    }
}
