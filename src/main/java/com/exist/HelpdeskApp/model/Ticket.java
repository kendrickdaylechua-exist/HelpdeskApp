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
    private Integer ticketNumber;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String body;

    @ManyToOne
    @JoinColumn(name = "assignee_id", nullable = false)
    private Employee assignee;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TicketStatus status;

    @CreatedDate
    @Column(nullable = false)
    private Instant createDate;

    @ManyToOne
    @JoinColumn(name = "created_by_id", nullable = false)
    private Employee createdBy;

    @UpdateTimestamp
    private Instant updatedDate;

    @ManyToOne
    @JoinColumn(name = "updated_by_id", nullable = false)
    private Employee updatedBy;

    private String remarks;

    private boolean deleted;

    @Version
    private Integer version;
}
