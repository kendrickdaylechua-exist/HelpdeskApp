package com.exist.HelpdeskApp.repository;

import com.exist.HelpdeskApp.model.Employee;
import com.exist.HelpdeskApp.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee,Integer>, JpaSpecificationExecutor<Employee> {
    Optional<Employee> findByIdAndDeletedFalse(Integer id);
    boolean existsByRole(Role role);
}
