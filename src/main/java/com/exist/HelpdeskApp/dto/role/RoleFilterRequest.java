package com.exist.HelpdeskApp.dto.role;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleFilterRequest {
    @Min(0)
    private int page = 0;

    @Min(1)
    private int size = 5;
    private String sortBy = "id";

    @Pattern(regexp = "asc|desc", message = "sortDir must be 'asc' or 'desc'")
    private String sortDir = "asc";

    private String roleName;

    private boolean deleted;
}
