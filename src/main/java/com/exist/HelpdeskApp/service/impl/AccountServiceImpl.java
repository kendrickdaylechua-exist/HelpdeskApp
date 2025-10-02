package com.exist.HelpdeskApp.service.impl;

import com.exist.HelpdeskApp.dto.account.AccountListRequest;
import com.exist.HelpdeskApp.dto.account.AccountMapper;
import com.exist.HelpdeskApp.dto.account.AccountRequest;
import com.exist.HelpdeskApp.dto.account.AccountResponse;
import com.exist.HelpdeskApp.exception.businessexceptions.AccountNotFoundException;
import com.exist.HelpdeskApp.exception.businessexceptions.EmployeeNotFoundException;
import com.exist.HelpdeskApp.exception.businessexceptions.RoleNotFoundException;
import com.exist.HelpdeskApp.model.Account;
import com.exist.HelpdeskApp.model.Employee;
import com.exist.HelpdeskApp.model.SecurityRole;
import com.exist.HelpdeskApp.repository.AccountRepository;
import com.exist.HelpdeskApp.repository.EmployeeRepository;
import com.exist.HelpdeskApp.repository.SecurityRoleRepository;
import com.exist.HelpdeskApp.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;
    private final PasswordEncoder passwordEncoder;
    private final EmployeeRepository employeeRepository;
    private final SecurityRoleRepository securityRoleRepository;

    @Autowired
    public AccountServiceImpl(AccountRepository accountRepository,
                              AccountMapper accountMapper,
                              PasswordEncoder passwordEncoder,
                              EmployeeRepository employeeRepository,
                              SecurityRoleRepository securityRoleRepository) {
        this.accountRepository = accountRepository;
        this.accountMapper = accountMapper;
        this.passwordEncoder = passwordEncoder;
        this.employeeRepository = employeeRepository;
        this.securityRoleRepository = securityRoleRepository;
    }

    @Transactional
    public Page<AccountResponse> getAccounts(AccountListRequest request) {
        Sort sort = request.getSortDir().equalsIgnoreCase("desc") ? Sort.by(request.getSortBy()).descending() : Sort.by(request.getSortBy()).ascending();
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize(), sort);
        Page<Account> accountPage = accountRepository.findAll(pageable);
        List<AccountResponse> accountResponses = accountMapper.toResponseList(accountPage.getContent());
        return new PageImpl<>(accountResponses, pageable, accountPage.getTotalElements());
    }

    @Transactional
    public AccountResponse createAccount(AccountRequest request) {
        Account account = new Account();
        account.setUsername(request.getUsername());
        account.setPassword(passwordEncoder.encode(request.getPassword()));

        Set<SecurityRole> roles = request.getSecurityRoles().stream()
                .map(roleName -> securityRoleRepository.findByName(roleName)
                        .orElseThrow(() -> new RoleNotFoundException("Role not found: " + roleName)))
                .collect(Collectors.toSet());

        account.setSecurityRoles(roles);

        if (request.getEmployeeId() != null) {
            Employee employee = checkEmployeeById(request.getEmployeeId());
            account.setEmployee(employee);
        }

        Account saved = accountRepository.save(account);
        return accountMapper.toResponse(saved);
    }

    @Transactional
    public AccountResponse updateAccount(String username, AccountRequest request, boolean dissociate) {
        Account account = checkAccountByUsername(username);
        if (dissociate) {
            Employee employee = account.getEmployee();
            if (employee != null) {
                account.detachEmployee();
            }
        }
        else if (request.getEmployeeId() != null) {
            Employee employee =  checkEmployeeById(request.getEmployeeId());
            account.attachEmployee(employee);
        }
        if (!request.getSecurityRoles().isEmpty()) {
            Set<SecurityRole> securityRoles = request.getSecurityRoles().stream()
                    .map(roleName -> securityRoleRepository.findByName(roleName)
                            .orElseThrow(() -> new RoleNotFoundException("Security Role Not found!")))
                    .collect(Collectors.toSet());
            account.setSecurityRoles(securityRoles);
        }

        Account updated = accountRepository.save(account);
        return accountMapper.toResponse(updated);
    }

    @Transactional
    public AccountResponse updateStatus(String username, boolean enabled) {
        Account account = checkAccountByUsername(username);
        account.setEnabled(enabled);
        Account updated = accountRepository.save(account);
        return accountMapper.toResponse(updated);
    }

    @Transactional
    public AccountResponse getAccountByUsername(String username) {
        Account account = checkAccountByUsername(username);
        return accountMapper.toResponse(account);
    }

    private Account checkAccountByUsername(String username) throws AccountNotFoundException{
        return accountRepository.findByUsername(username)
                .orElseThrow(() -> new AccountNotFoundException("Account with username " + username + " either not found or deleted!"));
    }

    private Employee checkEmployeeById(Integer id) {
        return employeeRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new EmployeeNotFoundException("Employee with ID " + id + " either not found or deleted!"));
    }
}
