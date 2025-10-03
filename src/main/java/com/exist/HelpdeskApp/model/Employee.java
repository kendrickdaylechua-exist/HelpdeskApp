package com.exist.HelpdeskApp.model;

import com.exist.HelpdeskApp.model.embeddable.Address;
import com.exist.HelpdeskApp.model.embeddable.Contacts;
import com.exist.HelpdeskApp.model.embeddable.Name;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    @Embedded
    private Name name;

    private Integer age;

    @Embedded
    private Address address;

    @Embedded
    private Contacts contacts;

    @Enumerated(EnumType.STRING)
    private EmploymentStatus employmentStatus;

    @ManyToOne
    private Role role;

    private boolean deleted;

//    @OneToOne(mappedBy = "employee", cascade = CascadeType.ALL)
//    @JsonBackReference
//    private Account account;

    @Version
    private Integer version;
}