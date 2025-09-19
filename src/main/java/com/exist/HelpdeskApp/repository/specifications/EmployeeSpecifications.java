package com.exist.HelpdeskApp.repository.specifications;

import com.exist.HelpdeskApp.model.Employee;
import com.exist.HelpdeskApp.model.EmploymentStatus;
import com.exist.HelpdeskApp.util.StringConverters;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;

public class EmployeeSpecifications {

//    private static Predicate buildStringPredicate(CriteriaBuilder cb, Path<String> path, String value, MatchType type) {
//        if (value == null || type == null) return null;
//
//        switch(type) {
//            case EXACT:
//                return cb.equal(cb.lower(path), value.toLowerCase());
//            case CONTAINS:
//                return cb.like(cb.lower(path), StringConverters.likePattern(value));
//            default:
//                return null;
//        }
//    }

    public static Specification<Employee> hasFirstName(String value) {
        return (root, query, cb) -> cb.like(root.get("name").get("firstName"), value);
    }

    public static Specification<Employee> hasMiddleName(String value) {
        return (root, query, cb) -> cb.like(root.get("name").get("middleName"), value);
    }

    public static Specification<Employee> hasLastName(String value) {
        return (root, query, cb) -> cb.like(root.get("name").get("lastName"), value);
    }

    public static Specification<Employee> nameContains(String value) {
        return (root, query, cb) -> {
            if (value == null) return null;
            String pattern = StringConverters.likePattern(value);
            return cb.or(
                    cb.like(cb.lower(root.get("name").get("firstName")), pattern),
                    cb.like(cb.lower(root.get("name").get("middleName")), pattern),
                    cb.like(cb.lower(root.get("name").get("lastName")), pattern)
            );
        };
    }

    //address
    public static Specification<Employee> hasStreet(String value) {
        return (root, query, cb) -> value == null ? null : cb.like(cb.lower(root.get("address").get("street")), StringConverters.likePattern(value));
    }

    public static Specification<Employee> hasCity(String value) {
        return (root, query, cb) -> value == null ? null : cb.like(cb.lower(root.get("address").get("city")), StringConverters.likePattern(value));
    }

    public static Specification<Employee> hasRegion(String value) {
        return (root, query, cb) -> value == null ? null : cb.like(cb.lower(root.get("address").get("region")), StringConverters.likePattern(value));
    }

    public static Specification<Employee> hasCountry(String value) {
        return (root, query, cb) -> value == null ? null : cb.like(cb.lower(root.get("address").get("country")), StringConverters.likePattern(value));
    }

    public static Specification<Employee> addressContains(String value) {
        return (root, query, cb) -> {
            if (value == null) return null;
            String pattern = StringConverters.likePattern(value);
            return cb.or(
                    cb.like(cb.lower(root.get("address").get("street")), pattern),
                    cb.like(cb.lower(root.get("address").get("city")), pattern),
                    cb.like(cb.lower(root.get("address").get("region")), pattern),
                    cb.like(cb.lower(root.get("address").get("country")), pattern)
            );
        };
    }

    //contacts
    public static Specification<Employee> hasPhoneNumber(String value) {
        return (root, query, cb) -> value == null ? null : cb.like(cb.lower(root.get("contacts").get("phoneNumber")), StringConverters.likePattern(value));
    }

    public static Specification<Employee> hasEmail(String value) {
        return (root, query, cb) -> value == null ? null : cb.like(cb.lower(root.get("contacts").get("email")), StringConverters.likePattern(value));
    }

    public static Specification<Employee> hasTelephoneNumber(String value) {
        return (root, query, cb) -> value == null ? null : cb.like(cb.lower(root.get("contacts").get("telephoneNumber")), StringConverters.likePattern(value));
    }
    public static Specification<Employee> contactContains(String value) {
        return (root, query, cb) -> {
            if (value == null) return null;
            String pattern = StringConverters.likePattern(value);
            return cb.or(
                    cb.like(cb.lower(root.get("contacts").get("phoneNumber")), pattern),
                    cb.like(cb.lower(root.get("contacts").get("email")), pattern),
                    cb.like(cb.lower(root.get("contacts").get("telephoneNumber")), pattern)
            );
        };
    }

    //employment status
    public static Specification<Employee> hasEmploymentStatus(String value) {
        return (root, query, cb) -> {
            if (value == null) return null;
            try {
                EmploymentStatus status = EmploymentStatus.valueOf(value.toUpperCase());
                return cb.equal(root.get("employmentStatus"), status);
            } catch (IllegalArgumentException e) {
                return cb.disjunction();
            }
        };
    }

    //role
    public static Specification<Employee> hasRoleId(Integer value) {
        return (root, query, cb) -> value == null ? null : cb.equal(root.get("role").get("id"), value);
    }

    public static Specification<Employee> hasRoleName(String value) {
        return (root, query, cb) -> value == null ? null : cb.like(cb.lower(root.get("role").get("roleName")), StringConverters.likePattern(value));
    }

    //deleted
    public static Specification<Employee> hasDeleted(boolean value) {
        return (root, query, cb) -> cb.equal(root.get("deleted"), value);
    }
}
