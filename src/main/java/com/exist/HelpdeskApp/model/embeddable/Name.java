package com.exist.HelpdeskApp.model.embeddable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class Name {
    private String firstName;
    private String middleName;
    private String lastName;
}
