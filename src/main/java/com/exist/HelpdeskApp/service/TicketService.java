package com.exist.HelpdeskApp.service;

import com.exist.HelpdeskApp.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TicketService {
    EmployeeRepository employeeRepository;

    @Autowired
    public TicketService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }
}
