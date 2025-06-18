package com.ducmoba.test_service.domain.dto.request;

import com.ducmoba.test_service.validator.DobConstraint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateRequest {
    private String userName ;
    private String firstName, lastName;
    private String password;
    @DobConstraint(min = 18, message = "INVALID_DOB")
    private LocalDate dob;
    private Set<String> roles;
}
