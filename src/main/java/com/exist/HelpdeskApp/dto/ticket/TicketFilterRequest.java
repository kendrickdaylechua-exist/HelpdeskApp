package com.exist.HelpdeskApp.dto.ticket;

import com.exist.HelpdeskApp.model.Ticket;
import com.exist.HelpdeskApp.util.StringConverters;
import liquibase.util.StringUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

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

    public Specification<Ticket> toSpec() {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.equal(root.get("deleted"), this.deleted));

            if (StringUtil.isNotEmpty(this.title)) {
                predicates.add(cb.like(cb.lower(root.get("title")), StringConverters.likePattern(this.title)));
            }

            if (StringUtil.isNotEmpty(this.body)) {
                predicates.add(cb.like(cb.lower(root.get("body")), StringConverters.likePattern(this.body)));
            }

            if (assigneeId != null) {
                predicates.add(cb.equal(root.get("assignee").get("id"), this.assigneeId));
            }

            if (StringUtil.isNotEmpty(this.assigneeName)) {
                String pattern = StringConverters.likePattern(this.assigneeName);
                predicates.add(cb.or(
                        cb.like(cb.lower(root.get("assignee").get("name").get("firstName")), pattern),
                        cb.like(cb.lower(root.get("assignee").get("name").get("middleName")), pattern),
                        cb.like(cb.lower(root.get("assignee").get("name").get("lastName")), pattern)
                ));
            }

            if (this.createdAfter != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("createDate"), this.createdAfter));
            }

            if (this.createdBefore != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("createDate"), this.createdAfter));
            }

            if (this.createdAfter != null && this.createdBefore != null) {
                predicates.add(cb.between(root.get("createdDate"), this.createdAfter, this.createdBefore));
            }

            if (this.creatorId != null) {
                predicates.add(cb.equal(root.get("createdBy").get("id"), this.creatorId));
            }

            if (StringUtil.isNotEmpty(this.creatorName)) {
                String pattern = StringConverters.likePattern(this.creatorName);
                predicates.add(cb.or(
                        cb.like(cb.lower(root.get("createdBy").get("name").get("firstName")), pattern),
                        cb.like(cb.lower(root.get("createdBy").get("name").get("middleName")), pattern),
                        cb.like(cb.lower(root.get("createdBy").get("name").get("lastName")), pattern)
                ));
            }

            if (this.updatedAfter != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("updatedDate"), this.updatedAfter));
            }

            if (this.updatedBefore != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("updatedDate"), this.updatedAfter));
            }

            if (this.updatedAfter != null && this.updatedBefore != null) {
                predicates.add(cb.between(root.get("updatedDate"), this.updatedAfter, this.updatedBefore));
            }

            if (this.updaterId != null) {
                predicates.add(cb.equal(root.get("updaterBy").get("id"), this.updaterId));
            }

            if (StringUtil.isNotEmpty(this.updaterName)) {
                String pattern = StringConverters.likePattern(this.updaterName);
                predicates.add(cb.or(
                        cb.like(cb.lower(root.get("updatedBy").get("name").get("firstName")), pattern),
                        cb.like(cb.lower(root.get("updatedBy").get("name").get("middleName")), pattern),
                        cb.like(cb.lower(root.get("updatedBy").get("name").get("lastName")), pattern)
                ));
            }

            if (StringUtil.isNotEmpty(this.remarks)) {
                predicates.add(cb.like(cb.lower(root.get("remarks")), StringConverters.likePattern(this.remarks)));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

}
