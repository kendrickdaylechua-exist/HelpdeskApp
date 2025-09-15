package com.exist.HelpdeskApp.repository;

import com.exist.HelpdeskApp.model.Employee;
import com.exist.HelpdeskApp.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface TicketRepository extends JpaRepository<Ticket, Integer>, JpaSpecificationExecutor<Ticket> {
    List<Ticket> findByAssigneeIdAndDeletedFalse(Integer employeeId);
    List<Ticket> findAllByDeletedFalse();
    Optional<Ticket> findByTicketNumberAndDeletedFalse(Integer ticketNumber);
    boolean existsByAssignee(Employee employee);
}
