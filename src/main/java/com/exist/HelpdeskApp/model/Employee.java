package com.exist.HelpdeskApp.model;

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
    private int employeeId;
    private String name;
    private int age;
    private String address;
    private String contactNumber;
    @Enumerated(EnumType.STRING)
    private EmploymentStatus employmentStatus;
//    @OneToOne
//    private Role role;
    @Version
    private int version;

    @Override
    public String toString() {
        return "Employee{" +
                "employeeId=" + employeeId +
                "name=" + name +
                "age=" + age +
                "address=" + address +
                "contactNumber=" + contactNumber +
                "employmentStatus=" + employmentStatus +
                "version=" + version +
                "}";
    }
}