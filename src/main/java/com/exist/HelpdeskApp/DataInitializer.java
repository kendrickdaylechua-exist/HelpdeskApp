package com.exist.HelpdeskApp;

import com.exist.HelpdeskApp.model.*;
import com.exist.HelpdeskApp.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.Set;

@Component
public class DataInitializer implements CommandLineRunner {

    private final PermissionRepository permissionRepository;
    private final SecurityRoleRepository securityRoleRepository;
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    private final Set<Permission> userPermissions = new HashSet<>();
    private final Set<Permission> adminPermissions = new HashSet<>();

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

        //==========Accounts============
        seedPermission("READ_ALL_ACCOUNTS", false, true);
        seedPermission("CREATE_ACCOUNT", false, true);
        seedPermission("READ_ACCOUNT", true, true);
        seedPermission("UPDATE_USERNAME_PASSWORD", true, false);
        seedPermission("ACCOUNT_ASSIGN_EMPLOYEE", false, true);
        seedPermission("ACCOUNT_ENABLE_DISABLE", false, true);
        seedPermission("ACCOUNT_SET_SECURITY_ROLE", false, true);

        //=========Employees============
        seedPermission("READ_SELF_EMPLOYEE", true, true);
        seedPermission("READ_ALL_EMPLOYEES", false, true);
        seedPermission("CREATE_EMPLOYEE", false, true);
        seedPermission("UPDATE_OTHER_EMPLOYEE", false, true);
        seedPermission("UPDATE_SELF_EMPLOYEE", true, true);
        seedPermission("DELETE_EMPLOYEE", false, true);
        seedPermission("UPDATE_EMPLOYEE_ROLE", false, true);

        //=========Roles============
        seedPermission("READ_ALL_ROLES", false, true);
        seedPermission("CREATE_ROLE", false, true);
        seedPermission("UPDATE_ROLE", false, true);
        seedPermission("DELETE_ROLE", false, true);

        //=========Tickets============
        seedPermission("FILE_TICKET", true, false);
        seedPermission("GET_ALL_TICKETS", true, true);
        seedPermission("GET_ASSIGNED_TICKETS", true, true);
        seedPermission("GET_OTHER_TICKETS", true, true);
        seedPermission("UPDATE_ASSIGNED_TICKETS", true, true);
        seedPermission("UPDATE_ANY_TICKETS", false, true);
        seedPermission("REASSIGNED_TICKET", false, true);


//==========================================================================================================
        SecurityRole adminRole = securityRoleRepository.findByName("ADMIN")
            .orElseGet(() -> {
                SecurityRole role = new SecurityRole();
                role.setName("ADMIN");
                return securityRoleRepository.save(role);
            });

        SecurityRole userRole = securityRoleRepository.findByName("USER")
            .orElseGet(() -> {
                SecurityRole role = new SecurityRole();
                role.setName("USER");
                return securityRoleRepository.save(role);
            });

//        adminRole.getPermissions().clear();
//        userRole.getPermissions().clear();
//
//        adminRole.getPermissions().addAll(adminPermissions);
//        userRole.getPermissions().addAll(userPermissions);
//
//        securityRoleRepository.save(adminRole);
//        securityRoleRepository.save(userRole);

//=========================================================================================================
        if (accountRepository.findByUsername("admin").isEmpty()) {
            Account admin = new Account();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setEnabled(true);
            admin.setSecurityRoles(Set.of(adminRole));
            accountRepository.save(admin);
        }
        adminRole.getPermissions().addAll(adminPermissions);
        securityRoleRepository.save(adminRole);
    }

    private void seedPermission(String name, boolean userPerm, boolean adminPerm) {
        Permission tempPermission = permissionRepository.findByName(name)
            .orElseGet(() -> {
                Permission permission = new Permission();
                permission.setName(name);
                return permissionRepository.save(permission);
            });
        if (userPerm) userPermissions.add(tempPermission);
        if (adminPerm) adminPermissions.add(tempPermission);
    }
}

