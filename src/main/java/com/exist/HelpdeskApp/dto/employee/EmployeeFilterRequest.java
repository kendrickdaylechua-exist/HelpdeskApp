package com.exist.HelpdeskApp.dto.employee;

import com.exist.HelpdeskApp.repository.specifications.MatchType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeFilterRequest {
    @Min(0)
    private int page = 0;

    @Min(1)
    private int size = 5;
    private String sortBy = "id";

    @Pattern(regexp = "asc|desc", message = "sortDir must be 'asc' or 'desc'")
    private String sortDir = "asc";

    private String firstName;
    private MatchType firstNameMatchType = MatchType.CONTAINS;
    private String middleName;
    private MatchType middleNameMatchType = MatchType.CONTAINS;
    private String lastName;
    private MatchType lastNameMatchType = MatchType.CONTAINS;
    private String nameKeyword;

    private String street;
    private String city;
    private String region;
    private String country;
    private String addressKeyword;

    private String phoneNumber;
    private String email;
    private String telephoneNumber;
    private String contactsKeyword;

    private String employmentStatus;

    private Integer roleId;
    private String roleName;

    private boolean deleted = false;
}
