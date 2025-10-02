package com.exist.HelpdeskApp.dto.account;

import com.exist.HelpdeskApp.model.Account;
import com.exist.HelpdeskApp.model.Employee;
import org.mapstruct.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface AccountMapper {

    @Mapping(target = "enabled", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "securityRoles", ignore = true)
    Account toEntity(AccountRequest request);

    @Mapping(source = "employee", target = "employeeName", qualifiedByName = "fullName")
    AccountResponse toResponse(Account account);

    List<AccountResponse> toResponseList(List<Account> accounts);

    @Mapping(target = "securityRoles", ignore = true)
    void toUpdate(AccountRequest request, @MappingTarget Account entity);

    @Named("fullName")
    static String mapFullName(Employee employee) {
        if (employee == null || employee.getName() == null) return null;

        String first = employee.getName().getFirstName();
        String middle = employee.getName().getMiddleName();
        String last = employee.getName().getLastName();

        return Stream.of(first, middle, last)
                .filter(s -> s != null && !s.isBlank())
                .collect(Collectors.joining(" "));
    }
}
