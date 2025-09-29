package com.exist.HelpdeskApp;

import com.exist.HelpdeskApp.model.*;
import com.exist.HelpdeskApp.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

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

    @Override
    public void run(String... args) throws Exception {

        Permission readAllAccounts = permissionRepository.findByName("READ_ALL_ACCOUNTS")
                        .orElseGet(() -> {
                            Permission permission = new Permission();
                            permission.setName("READ_ALL_ACCOUNTS");
                            return permissionRepository.save(permission);
                        });

        SecurityRole adminRole = securityRoleRepository.findByName("ROLE_ADMIN")
                .orElseGet(() -> {
                    SecurityRole role = new SecurityRole();
                    role.setName("ROLE_ADMIN");
                    role.setPermissions(Set.of(readAllAccounts));
                    return securityRoleRepository.save(role);
                });

        securityRoleRepository.findByName("ROLE_USER")
                .orElseGet(() -> {
                    SecurityRole role = new SecurityRole();
                    role.setName("ROLE_USER");
                    return securityRoleRepository.save(role);
                });

        if (accountRepository.findByUsername("admin").isEmpty()) {
            Account admin = new Account();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123")); // encode the password
            admin.setEnabled(true);
            admin.setSecurityRoles(Set.of(adminRole));
            accountRepository.save(admin);
        }
    }
}

