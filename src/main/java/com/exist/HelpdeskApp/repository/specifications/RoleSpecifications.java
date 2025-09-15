package com.exist.HelpdeskApp.repository.specifications;

import com.exist.HelpdeskApp.model.Role;
import com.exist.HelpdeskApp.util.StringConverters;
import org.springframework.data.jpa.domain.Specification;

public class RoleSpecifications {
    public static Specification<Role> hasRoleName(String value) {
        return (root, query, cb) -> value == null ? null : cb.like(root.get("roleName"), StringConverters.likePattern(value));
    }

    public static Specification<Role> hasDeleted(boolean value) {
        return (root, query, cb) -> cb.equal(root.get("deleted"), value);
    }
}
