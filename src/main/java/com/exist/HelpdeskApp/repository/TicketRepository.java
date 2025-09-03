package com.exist.HelpdeskApp.service.repository;

import com.exist.HelpdeskApp.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepository extends JpaRepository<Ticket, Integer> {
}
