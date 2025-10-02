package com.exist.HelpdeskApp.dto.employee;

//import com.exist.HelpdeskApp.repository.specifications.MatchType;
import com.exist.HelpdeskApp.model.Employee;
import com.exist.HelpdeskApp.model.EmploymentStatus;
import com.exist.HelpdeskApp.util.StringConverters;
import liquibase.util.StringUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Formula;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeFilterRequest {
//    @Min(0)
//    private int page = 0;
//
//    @Min(1)
//    @Max(10)
//    private int size = 5;
//    private String sortBy = "id";
//
//    @Pattern(regexp = "asc|desc", message = "sortDir must be 'asc' or 'desc'")
//    private String sortDir = "asc";

    private String name;

    private String age;

    private String address;

    private String contacts;

    private String status;

    private Integer roleId;
    private String roleName;

    private boolean deleted = false;

    public Specification<Employee> toSpec() {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.equal(root.get("deleted"), this.deleted));

            if (StringUtil.isNotEmpty(this.name)) {
                Expression<String> fullName = cb.concat(
                        cb.concat(
                                cb.concat(root.get("name").get("firstName"), " "),
                                cb.concat(root.get("name").get("middleName"), " ")
                        ), root.get("name").get("lastName")
                );
                String pattern = StringConverters.likePattern(this.name);
                predicates.add(cb.like(fullName, pattern));
            }

            if (this.age != null) {
                predicates.add(cb.equal(root.get("age"), this.age));
            }

            if (StringUtil.isNotEmpty(this.address)) {
                String pattern = StringConverters.likePattern(this.address);
                predicates.add(cb.or(
                        cb.like(cb.lower(root.get("address").get("street")), pattern),
                        cb.like(cb.lower(root.get("address").get("city")), pattern),
                        cb.like(cb.lower(root.get("address").get("region")), pattern),
                        cb.like(cb.lower(root.get("address").get("country")), pattern)
                ));
            }

            if (StringUtil.isNotEmpty(this.contacts)) {
                String pattern = StringConverters.likePattern(this.contacts);
                predicates.add(cb.or(
                        cb.like(cb.lower(root.get("contacts").get("phoneNumber")), pattern),
                        cb.like(cb.lower(root.get("contacts").get("email")), pattern),
                        cb.like(cb.lower(root.get("contacts").get("telephoneNumber")), pattern)
                ));
            }

            if (StringUtil.isNotEmpty(this.status)) {
                String keyword = this.status.trim().toUpperCase();

                Optional<EmploymentStatus> matchedStatus = Arrays.stream(EmploymentStatus.values())
                        .filter(s -> s.name().contains(keyword))
                        .findFirst();

                matchedStatus.ifPresent(status ->
                        predicates.add(cb.equal(root.get("employmentStatus"), status))
                );
            }

            if (this.roleId != null) {
                predicates.add(cb.equal(root.get("role").get("id"), this.roleId));
            }

            if (StringUtil.isNotEmpty(this.roleName)) {
                String pattern = StringConverters.likePattern(this.roleName);
                predicates.add(cb.like(cb.lower(root.get("role").get("roleName")), pattern));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
