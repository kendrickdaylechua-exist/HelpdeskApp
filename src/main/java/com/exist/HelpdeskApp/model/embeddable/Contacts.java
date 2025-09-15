package com.exist.HelpdeskApp.model.embeddable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class Contacts {
    private String phoneNumber;
    private String email;
    private String telephoneNumber;
}
