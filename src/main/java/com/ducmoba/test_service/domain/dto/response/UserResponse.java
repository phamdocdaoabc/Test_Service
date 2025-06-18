package com.ducmoba.test_service.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {
    private String id;

    private String userName;

    private String password;

    private String firstName;

    private String lastName;

    private LocalDate dob;

    private String email;

    private String phone;

    private Set<RoleResponse> roles;
}
