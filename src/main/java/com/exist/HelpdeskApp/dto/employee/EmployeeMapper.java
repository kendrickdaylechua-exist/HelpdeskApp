package com.exist.HelpdeskApp.dto.employee;

import com.exist.HelpdeskApp.model.Employee;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.data.domain.Page;

import java.util.List;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface EmployeeMapper {
    //Request DTO to Entity
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "deleted", ignore = true)
//    @Mapping(source = "contacts", target = "contact")
    Employee toEntity(EmployeeRequest request);

    //Entity to Response DTO
    @Mapping(source = "role.roleName", target = "roleName")
    @Mapping(source = "role.id", target = "roleId")
    EmployeeResponse toResponse(Employee employee);

    void toUpdate(EmployeeRequest request, @MappingTarget Employee entity);

    List<EmployeeResponse> toResponseList(List<Employee> employees);
}
