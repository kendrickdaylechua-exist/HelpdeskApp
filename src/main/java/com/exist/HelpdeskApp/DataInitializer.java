package com.exist.HelpdeskApp;

import com.exist.HelpdeskApp.model.*;
import com.exist.HelpdeskApp.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Set;

@Component
public class DataInitializer implements CommandLineRunner {

    private final PermissionRepository permissionRepository;
    private final SecurityRoleRepository securityRoleRepository;
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public DataInitializer(PermissionRepository permissionRepo,
                           SecurityRoleRepository roleRepo,
                           AccountRepository userRepo,
                           PasswordEncoder passwordEncoder) {
        this.permissionRepository = permissionRepo;
        this.securityRoleRepository = roleRepo;
        this.accountRepository = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    @Override
    public void run(String... args) throws Exception {

        List<Permission> allPermissions = permissionRepository.findAll();

        Permission readAllAccounts = permissionRepository.findByName("READ_ALL_ACCOUNTS")
                        .orElseGet(() -> {
                            Permission permission = new Permission();
                            permission.setName("READ_ALL_ACCOUNTS");
                            return permissionRepository.save(permission);
                        });

        Permission readEmployee = permissionRepository.findByName("READ_EMPLOYEE")
                .orElseGet(() -> {
                    Permission permission = new Permission();
                    permission.setName("READ_EMPLOYEE");
                    return permissionRepository.save(permission);
                });

        Permission readAllEmployees = permissionRepository.findByName("READ_ALL_EMPLOYEES")
                .orElseGet(() -> {
                    Permission permission = new Permission();
                    permission.setName("READ_ALL_EMPLOYEES");
                    return permissionRepository.save(permission);
                });

        Permission createEmployee = permissionRepository.findByName("CREATE_EMPLOYEE")
                .orElseGet(() -> {
                    Permission permission = new Permission();
                    permission.setName("CREATE_EMPLOYEE");
                    return permissionRepository.save(permission);
                });

        Permission updateEmployee = permissionRepository.findByName("UPDATE_EMPLOYEE")
                .orElseGet(() -> {
                    Permission permission = new Permission();
                    permission.setName("UPDATE_EMPLOYEE");
                    return permissionRepository.save(permission);
                });

        Permission deleteEmployee = permissionRepository.findByName("DELETE_EMPLOYEE")
                .orElseGet(() -> {
                    Permission permission = new Permission();
                    permission.setName("DELETE_EMPLOYEE");
                    return permissionRepository.save(permission);
                });

        SecurityRole adminRole = securityRoleRepository.findByName("ROLE_ADMIN")
                .orElseGet(() -> {
                    SecurityRole role = new SecurityRole();
                    role.setName("ROLE_ADMIN");
                    return securityRoleRepository.save(role);
                });

        securityRoleRepository.findByName("ROLE_USER")
                .orElseGet(() -> {
                    SecurityRole role = new SecurityRole();
                    role.setName("ROLE_USER");
//                    role.setPermissions(Set.of(
//                            readEmployee,
//                    ));
                    return securityRoleRepository.save(role);
                });

        if (accountRepository.findByUsername("admin").isEmpty()) {
            Account admin = new Account();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setEnabled(true);
            admin.setSecurityRoles(Set.of(adminRole));
            accountRepository.save(admin);
        }
        adminRole.getPermissions().addAll(allPermissions);
        securityRoleRepository.save(adminRole);
    }
}

