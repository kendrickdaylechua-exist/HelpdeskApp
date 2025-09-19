package com.exist.HelpdeskApp.dto.embedded;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContactsRequest {

    @NotBlank(message = "Phone number cannot be empty")
    @Size(max = 16, message = "There is no phone number that exceeds 16 digits")
    private String phoneNumber;

    @NotBlank(message = "Email cannot be empty")
    @Email(message = "Invalid email format")
    private String email;

    @Size(max = 16, message = "There is no telephone number that exceeds 16 digits")
    private String telephoneNumber;
}
