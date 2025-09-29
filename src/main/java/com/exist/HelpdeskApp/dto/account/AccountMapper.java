package com.exist.HelpdeskApp.dto.account;

import com.exist.HelpdeskApp.model.Account;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface AccountMapper {

//    @Autowired
//    protected PasswordEncoder passwordEncoder;


    @Mapping(target = "enabled", ignore = true)
    @Mapping(target = "password", ignore = true)
    Account toEntity(AccountRequest request);

    @Mapping(source = "employee.name.firstName", target = "employeeName")
    AccountResponse toResponse(Account account);

    List<AccountResponse> toResponseList(List<Account> accounts);

    void toUpdate(AccountRequest request, @MappingTarget Account entity);

//    @AfterMapping
//    protected void encodePassword(AccountRequest request, @MappingTarget Account account) {
//        if (request.getPassword() != null) {
//            account.setPassword(passwordEncoder.encode(request.getPassword()));
//        }
//    }
}
