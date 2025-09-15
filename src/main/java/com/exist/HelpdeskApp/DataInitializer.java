package com.exist.HelpdeskApp;

import com.exist.HelpdeskApp.exception.businessexceptions.RoleNotFoundException;
import com.exist.HelpdeskApp.model.Employee;
import com.exist.HelpdeskApp.model.Role;
import com.exist.HelpdeskApp.model.EmploymentStatus;
import com.exist.HelpdeskApp.model.embeddable.Name;
import com.exist.HelpdeskApp.repository.EmployeeRepository;
import com.exist.HelpdeskApp.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final EmployeeRepository employeeRepository;

    @Autowired
    public DataInitializer(RoleRepository roleRepository, EmployeeRepository employeeRepository) {
        this.roleRepository = roleRepository;
        this.employeeRepository = employeeRepository;
    }

    @Override
    public void run(String... args) throws Exception {

        // Initialize roles if none exist
        if(roleRepository.count() == 0) {
            Role adminRole = new Role();
            adminRole.setRoleName("Admin");
            roleRepository.save(adminRole);
        }

        // Initialize employees if none exist
        if(employeeRepository.count() == 0) {
            Employee adminEmployee = new Employee();
            adminEmployee.setName(new Name("Admin", null, null));
            adminEmployee.setAge(30);
            adminEmployee.setEmploymentStatus(EmploymentStatus.FULL_TIME);
            adminEmployee.setRole(roleRepository.findById(1)
                    .orElseThrow(() -> new RoleNotFoundException("Admin role not found!")));
            employeeRepository.save(adminEmployee);
        }
    }
}

