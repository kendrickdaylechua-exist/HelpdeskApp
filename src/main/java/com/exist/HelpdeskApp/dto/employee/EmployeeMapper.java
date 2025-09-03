package com.exist.HelpdeskApp.dto.employee;

import com.exist.HelpdeskApp.model.Employee;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface EmployeeMapper {
    //Request DTO to Entity
    Employee toEntity(EmployeeRequest request);

    //Entity to Response DTO
    EmployeeResponse toResponse(Employee employee);

    void toUpdate(EmployeeRequest request, @MappingTarget Employee entity);

    List<EmployeeResponse> toResponseList(List<Employee> employees);
}
