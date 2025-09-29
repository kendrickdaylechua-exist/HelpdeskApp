package com.exist.HelpdeskApp.dto.account;

import com.exist.HelpdeskApp.model.Account;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface AccountMapper {

    @Mapping(target = "enabled", ignore = true)
    Account toEntity(AccountRequest request);

    @Mapping(source = "employee.name.firstName", target = "employeeName")
    AccountResponse toResponse(Account account);

    List<AccountResponse> toResponseList(List<Account> accounts);
}
