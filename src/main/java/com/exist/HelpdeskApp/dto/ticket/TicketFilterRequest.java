package com.exist.HelpdeskApp.dto.ticket;

import com.exist.HelpdeskApp.repository.specifications.MatchType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TicketFilterRequest {
    @Min(0)
    private int page = 0;

    @Min(1)
    private int size = 5;
    private String sortBy = "ticketNumber";

    @Pattern(regexp = "asc|desc", message = "sortDir must be 'asc' or 'desc'")
    private String sortDir = "asc";

    private String title;
    private MatchType titleMatchType = MatchType.CONTAINS;
    private String body;

    private String assigneeName;
    private Integer assigneeId;

    private String status;

    private Instant createdAfter;
    private Instant createdBefore;
    private String creatorName;
    private Integer creatorId;

    private Instant updatedAfter;
    private Instant updatedBefore;
    private String updaterName;
    private Integer updaterId;

    private String remarks;
    private boolean deleted;

}
