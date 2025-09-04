package com.exist.HelpdeskApp.repository;

import com.exist.HelpdeskApp.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket, Integer> {
    List<Ticket> findByAssigneeId(int employeeId);
}
