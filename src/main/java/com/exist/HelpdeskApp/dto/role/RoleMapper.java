package com.exist.HelpdeskApp.dto.role;
import com.exist.HelpdeskApp.model.Role;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface RoleMapper {
    //DTO to Entity
    Role toEntity(RoleRequest request);

    //Entity to DTO
    RoleResponse toResponse(Role entity);

    void toUpdate(RoleRequest request, @MappingTarget Role entity);

    List<RoleResponse> toResponseList(List<Role> roles);
}
