package com.exist.HelpdeskApp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int ticketNumber;
    private String title;
    private String body;
    @OneToOne
    private Employee assignee;
    @Enumerated(EnumType.STRING)
    private TicketStatus status;
    @CreatedDate
    private Instant createDate;
    @ManyToOne
    private Employee createdBy;
    @UpdateTimestamp
    private Instant updatedDate;
    @ManyToOne
    private Employee updatedBy;
    private String remarks;
    @Version
    private int version;
}
