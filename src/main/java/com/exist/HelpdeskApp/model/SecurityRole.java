package com.exist.HelpdeskApp.model;

import lombok.Data;

import javax.persistence.*;
import com.exist.HelpdeskApp.model.Permission;
import java.util.Set;

@Entity
@Table(name = "security_role")
@Data
public class SecurityRole {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "security_role_permissions",
            joinColumns = @JoinColumn(name = "security_role_id"),
            inverseJoinColumns = @JoinColumn(name = "permission_id")
    )
    private Set<Permission> permissions;
}
