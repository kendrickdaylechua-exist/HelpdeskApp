//package com.exist.HelpdeskApp.model;
//
//import com.exist.HelpdeskApp.repository.EmployeeRepository;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//
//@DataJpaTest
//class EmployeeTest {
//
//    @Autowired
//    private TestEntityManager entityManager;
//
//    @Autowired
//    private EmployeeRepository employeeRepository;
//
//    @Test
//    void testSaveAndRetrieveEmployee() {
//        // Persist Role
//        Role role = new Role();
//        role.setRoleName("Test Role");
//        Role savedRole = entityManager.persistAndFlush(role);
//
//        Employee employee = new Employee();
//        employee.setName("Test Name");
//        employee.setAge(30);
//        employee.setAddress("Test Address");
//        employee.setContactNumber("09~~~~~~~~~~");
//        employee.setEmploymentStatus(EmploymentStatus.FULL_TIME);
//        employee.setRole(savedRole);
//
//        Employee savedEmployee = employeeRepository.saveAndFlush(employee);
//
//        Employee retrieved = employeeRepository.findById(savedEmployee.getId()).orElseThrow();
//
//        assertEquals("Test Name", retrieved.getName());
//        assertEquals(30, retrieved.getAge());
//        assertEquals("Test Address", retrieved.getAddress());
//        assertEquals("09~~~~~~~~~~", retrieved.getContactNumber());
//        assertEquals(EmploymentStatus.FULL_TIME, retrieved.getEmploymentStatus());
//        assertEquals("Test Role", retrieved.getRole().getRoleName());
//    }
//
//    @Test
//    void testNotNullConstraints() {
//        Employee employee = new Employee();
//        assertThrows(Exception.class, () -> employeeRepository.save(employee));
//    }
//}
