package com.exist.HelpdeskApp.repository.specifications;

import com.exist.HelpdeskApp.model.Ticket;
import com.exist.HelpdeskApp.util.StringConverters;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import java.time.Instant;

public class TicketSpecifications {
    private static Predicate buildStringPredicate(CriteriaBuilder cb, Path<String> path, String value, MatchType type) {
        if (value == null || type == null) return null;

        switch (type) {
            case EXACT:
                return cb.equal(cb.lower(path), value.toLowerCase());
            case CONTAINS:
                return cb.like(cb.lower(path), StringConverters.likePattern(value.toLowerCase()));
            default:
                return null;
        }
    }

    public static Specification<Ticket> hasTitle(String value, MatchType matchType) {
        return (root, query, cb) -> buildStringPredicate(cb, root.get("title"), value, matchType);
    }

    public static Specification<Ticket> hasBody(String value) {
        return (root, query, cb) -> value == null ? null : cb.like(cb.lower(root.get("body")), StringConverters.likePattern(value));
    }

    public static Specification<Ticket> hasAssigneeId(Integer value) {
        return (root, query, cb) -> value == null ? null : cb.equal(root.get("assignee").get("id"), value);
    }

    public static Specification<Ticket> hasAssigneeName(String value) {
        return (root, query, cb) -> {
            if (value == null) return null;
            String pattern = StringConverters.likePattern(value);
            return cb.or(
                    cb.like(cb.lower(root.get("assignee").get("name").get("firstName")), pattern),
                    cb.like(cb.lower(root.get("assignee").get("name").get("middleName")), pattern),
                    cb.like(cb.lower(root.get("assignee").get("name").get("lastName")), pattern)
            );
        };
    }

    public static Specification<Ticket> hasCreatedAt(Instant value) {
        return (root, query, cb) -> value == null ? null : cb.equal(root.get("createDate"), value);
    }

    public static Specification<Ticket> hasCreatedAfter(Instant value) {
        return (root, query, cb) -> value == null ? null : cb.greaterThanOrEqualTo(root.get("createDate"), value);
    }

    public static Specification<Ticket> hasCreatedBefore(Instant value) {
        return (root, query, cb) -> value == null ? null : cb.lessThanOrEqualTo(root.get("createDate"), value);
    }

    public static Specification<Ticket> createdBetween(Instant start, Instant end) {
        return (root, query, cb) -> {
            if (start == null || end == null) return null;
            return cb.between(root.get("createdDate"), start, end);
        };
    }

    public static Specification<Ticket> hasCreatedId(Integer value) {
        return (root, query, cb) -> value == null ? null : cb.equal(root.get("createdBy").get("id"), value);
    }

    public static Specification<Ticket> hasCreatorName(String value) {
        return (root, query, cb) -> {
            if (value == null) return null;
            String pattern = StringConverters.likePattern(value);
            return cb.or(
                    cb.like(cb.lower(root.get("createdBy").get("name").get("firstName")), pattern),
                    cb.like(cb.lower(root.get("createdBy").get("name").get("middleName")), pattern),
                    cb.like(cb.lower(root.get("createdBy").get("name").get("lastName")), pattern)
            );
        };
    }

    public static Specification<Ticket> hasUpdatedAt(Instant value) {
        return (root, query, cb) -> value == null ? null : cb.equal(root.get("updatedDate"), value);
    }

    public static Specification<Ticket> hasUpdatedAfter(Instant value) {
        return (root, query, cb) -> value == null ? null : cb.greaterThanOrEqualTo(root.get("updatedDate"), value);
    }

    public static Specification<Ticket> hasUpdatedBefore(Instant value) {
        return (root, query, cb) -> value == null ? null : cb.lessThanOrEqualTo(root.get("updatedDate"), value);
    }

    public static Specification<Ticket> updatedBetween(Instant start, Instant end) {
        return (root, query, cb) -> {
            if (start == null || end == null) return null;
            return cb.between(root.get("updatedDate"), start, end);
        };
    }

    public static Specification<Ticket> hasUpdaterId(Integer value) {
        return (root, query, cb) -> value == null ? null : cb.equal(root.get("updatedBy").get("id"), value);
    }

    public static Specification<Ticket> hasUpdaterName(String value) {
        return (root, query, cb) -> {
            if (value == null) return null;
            String pattern = StringConverters.likePattern(value);
            return cb.or(
                    cb.like(cb.lower(root.get("updatedBy").get("name").get("firstName")), pattern),
                    cb.like(cb.lower(root.get("updatedBy").get("name").get("middleName")), pattern),
                    cb.like(cb.lower(root.get("updatedBy").get("name").get("lastName")), pattern)
            );
        };
    }

    public static Specification<Ticket> hasRemarks(String value) {
        return (root, query, cb) -> value == null ? null : cb.like(cb.lower(root.get("remarks")), StringConverters.likePattern(value));
    }
}
