package com.ducmoba.test_service.domain.dto.request;

import com.ducmoba.test_service.validator.DobConstraint;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserCreationRequest {
    @Size(min = 6, message = "USERNAME_INVALID")
    private String userName ;
    private String firstName, lastName;
    @Size(min = 8, message = "PASSWORD_INVALID")
    private String password;
    @DobConstraint(min = 18, message = "INVALID_DOB")
    private LocalDate dob;
    @Email(message = "Email_INVALID")
    private String email;
    private String phone;
}
