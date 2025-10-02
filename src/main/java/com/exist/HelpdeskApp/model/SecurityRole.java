package com.exist.HelpdeskApp.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;

import javax.persistence.*;
import com.exist.HelpdeskApp.model.Permission;

import java.util.HashSet;
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
    private Set<Permission> permissions = new HashSet<>();

//    @ManyToMany(mappedBy = "securityRoles")
//    @JsonIgnore
//    private Set<Account> accounts = new HashSet<>();
}
