package com.exist.HelpdeskApp.service.repository;

import com.exist.HelpdeskApp.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee,Integer> {
}
