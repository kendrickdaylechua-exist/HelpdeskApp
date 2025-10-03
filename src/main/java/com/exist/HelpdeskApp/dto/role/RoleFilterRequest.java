package com.exist.HelpdeskApp.dto.role;

import com.exist.HelpdeskApp.model.Role;
import com.exist.HelpdeskApp.util.StringConverters;
import liquibase.util.StringUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleFilterRequest {

    private String roleName;

    private boolean deleted;

    public Specification<Role> toSpec() {
        List<Predicate> predicates = new ArrayList<>();
        return (root, query, cb) -> {
            predicates.add(cb.equal(root.get("deleted"), this.deleted));
            if (StringUtil.isNotEmpty(this.roleName)) {
                predicates.add(cb.like(cb.lower(root.get("roleName")), StringConverters.likePattern(this.roleName)));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
