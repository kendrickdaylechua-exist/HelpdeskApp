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

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String body;

    @OneToOne
    @Column(nullable = false)
    private Employee assignee;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TicketStatus status;

    @CreatedDate
    @Column(nullable = false)
    private Instant createDate;

    @ManyToOne
    @Column(nullable = false)
    private Employee createdBy;

    @UpdateTimestamp
    private Instant updatedDate;

    @ManyToOne
    private Employee updatedBy;

    private String remarks;

    @Version
    private int version;
}
