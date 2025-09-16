//package com.exist.HelpdeskApp.repository;
//
//import com.exist.HelpdeskApp.model.Employee;
//import com.exist.HelpdeskApp.model.EmploymentStatus;
//import com.exist.HelpdeskApp.model.Role;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertTrue;
//
//@DataJpaTest
//public class EmployeeRepositoryTest {
//
//    @Autowired
//    EmployeeRepository employeeRepository;
//
//    @Test
//    void testSaveAndFindId() {
//
//        Employee employee = new Employee();
//        employee.setName("Name 1");
//        employee.setAge(30);
//        employee.setAddress("Test address");
//        employee.setContactNumber("09123456789");
//
//        employee = employeeRepository.save(employee);
//
//        Optional<Employee> found = employeeRepository.findById(employee.getId());
//
//        assertTrue(found.isPresent());
//        assertEquals("Name 1", found.get().getName());
//    }
//
//    @Test
//    void testDelete() {
//        Employee employee = new Employee();
//        employee.setName("Name 1");
//        employee.setAge(30);
//        employee.setAddress("Test address");
//        employee.setContactNumber("09123456789");
//        employee = employeeRepository.save(employee);
//
//        employeeRepository.delete(employee);
//
//        assertTrue(employeeRepository.findById(employee.getId()).isEmpty());
//    }
//
//}
