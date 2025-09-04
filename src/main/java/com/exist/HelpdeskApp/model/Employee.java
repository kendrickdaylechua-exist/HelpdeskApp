package com.exist.HelpdeskApp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer age;

    @Column(nullable = false)
    private String address;

    @NonNull
    @Column(nullable = false)
    private String contactNumber;

    @Enumerated(EnumType.STRING)
    private EmploymentStatus employmentStatus;

    @ManyToOne
    private Role role;

    @Version
    private int version;

    @Override
    public String toString() {
        return "Employee{" +
                "employeeId=" + id +
                "name=" + name +
                "age=" + age +
                "address=" + address +
                "contactNumber=" + contactNumber +
                "employmentStatus=" + employmentStatus +
                "version=" + version +
                "}";
    }
}