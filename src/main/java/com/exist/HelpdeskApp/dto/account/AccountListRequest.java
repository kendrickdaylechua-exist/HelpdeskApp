package com.exist.HelpdeskApp.dto.account;

import com.exist.HelpdeskApp.model.Account;
import com.exist.HelpdeskApp.util.StringConverters;
import liquibase.util.StringUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountListRequest {
    private String username;
    private boolean disabled;
    private Integer employeeId;
    private String securityRole;

    public Specification<Account> toSpec() {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (StringUtil.isNotEmpty(this.username)) {
                String pattern = StringConverters.likePattern(this.username);
                predicates.add(cb.like(root.get("username"), pattern));
            }

            if (this.disabled) {
                predicates.add(cb.equal(root.get("deleted"), this.disabled));
            }

            if (this.employeeId != null) {
                predicates.add(cb.equal(root.get("employee").get("id"), this.employeeId));
            }

            if (StringUtil.isNotEmpty(this.securityRole)) {
                String pattern = StringConverters.likePattern(this.securityRole);
                predicates.add(cb.like(root.get("username"), pattern));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };

    }
}
