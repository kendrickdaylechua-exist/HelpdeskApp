package com.exist.HelpdeskApp.service.impl;

import com.exist.HelpdeskApp.dto.account.AccountListRequest;
import com.exist.HelpdeskApp.dto.account.AccountMapper;
import com.exist.HelpdeskApp.dto.account.AccountRequest;
import com.exist.HelpdeskApp.dto.account.AccountResponse;
import com.exist.HelpdeskApp.exception.businessexceptions.*;
import com.exist.HelpdeskApp.model.Account;
import com.exist.HelpdeskApp.model.Employee;
import com.exist.HelpdeskApp.model.SecurityRole;
import com.exist.HelpdeskApp.repository.AccountRepository;
import com.exist.HelpdeskApp.repository.EmployeeRepository;
import com.exist.HelpdeskApp.repository.SecurityRoleRepository;
import com.exist.HelpdeskApp.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import javax.transaction.Transactional;
import java.util.List;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Consumer;
import java.util.stream.Collectors;

@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;
    private final PasswordEncoder passwordEncoder;
    private final EmployeeRepository employeeRepository;
    private final SecurityRoleRepository securityRoleRepository;
    private JwtServiceImpl jwtService;
    private AuthenticationManager authenticationManager;

    private static final Logger logger = LoggerFactory.getLogger(AccountService.class);

    @Autowired
    public AccountServiceImpl(AccountRepository accountRepository,
                              AccountMapper accountMapper,
                              PasswordEncoder passwordEncoder,
                              EmployeeRepository employeeRepository,
                              SecurityRoleRepository securityRoleRepository,
                              JwtServiceImpl jwtService,
                              AuthenticationManager authenticationManager) {
        this.accountRepository = accountRepository;
        this.accountMapper = accountMapper;
        this.passwordEncoder = passwordEncoder;
        this.employeeRepository = employeeRepository;
        this.securityRoleRepository = securityRoleRepository;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public Page<AccountResponse> getAccounts(AccountListRequest request) {
        Sort sort = request.getSortDir().equalsIgnoreCase("desc") ? Sort.by(request.getSortBy()).descending() : Sort.by(request.getSortBy()).ascending();
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize(), sort);
        Page<Account> accountPage = accountRepository.findAll(pageable);
        List<AccountResponse> accountResponses = accountMapper.toResponseList(accountPage.getContent());
        return new PageImpl<>(accountResponses, pageable, accountPage.getTotalElements());
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
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
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public AccountResponse updateAccount(String username, AccountRequest request, Authentication authentication, boolean dissociate) {
        Account account = checkAccountByUsername(username);

        Set<String> permissions = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());

        Consumer<String> requirePermission = perm -> {
            if (!permissions.contains(perm)) {
                logger.info("User " + authentication.getName() + " attempted to perform this action: " + perm);
                throw new AccessDeniedException("You do not have permission to perform this action");
            }
        };

        if (request.getEmployeeId() != null || dissociate) {
            requirePermission.accept("ACCOUNT_ASSIGN_EMPLOYEE");

            if (dissociate) {
                account.setEmployee(null);
            } else {
                Employee employee = checkEmployeeById(request.getEmployeeId());
                account.setEmployee(employee);
            }
        }

        if (request.getSecurityRoles() != null) {
            requirePermission.accept("ACCOUNT_SET_SECURITY_ROLE");

            Set<SecurityRole> securityRoles = request.getSecurityRoles().stream()
                    .map(roleName -> securityRoleRepository.findByName(roleName)
                            .orElseThrow(() -> new RoleNotFoundException("Security Role Not found!")))
                    .collect(Collectors.toSet());
            account.setSecurityRoles(securityRoles);
        }

        if ((request.getUsername() != null && !request.getUsername().isEmpty()) ||
                (request.getPassword() != null && !request.getPassword().isEmpty())) {

            requirePermission.accept("UPDATE_USERNAME_PASSWORD");
            if (request.getUsername() != null && !request.getUsername().isEmpty()) {
                account.setUsername(request.getUsername());
            }
            if (request.getPassword() != null && !request.getPassword().isEmpty()) {
                account.setPassword(passwordEncoder.encode(request.getPassword()));
            }
        }

        Account updated = accountRepository.save(account);
        return accountMapper.toResponse(updated);
    }


    @Transactional
    public AccountResponse updateStatus(String username, Authentication authentication, boolean enabled) {
        String adminUsername = authentication.getName();
        Account account = checkAccountByUsername(username);

        // Prevent disabling self
        if (account.getUsername().equals(adminUsername) && !enabled) {
            throw new AccessDeniedException("Cannot disable your own account");
        }

        account.setEnabled(enabled);
        Account updated = accountRepository.save(account);
        logger.info("Admin {} changed status of account {} to {}", adminUsername, updated, enabled ? "enabled." : "disabled.");
        return accountMapper.toResponse(updated);
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN') or #username == authentication.name")
    public AccountResponse getAccountByUsername(String username) {
        Account account = checkAccountByUsername(username);
        return accountMapper.toResponse(account);
    }

    @Transactional
    public String accountLogin(AccountRequest accountRequest) {
        Account account = checkAccountByUsername(accountRequest.getUsername());

        if (!account.isEnabled()) {
            throw new DissabledAccountException("Your account is disabled!");
        }

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        accountRequest.getUsername(),
                        accountRequest.getPassword()
                )
        );

        return jwtService.generateToken(account.getUsername());
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
