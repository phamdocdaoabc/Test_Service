package com.ducmoba.test_service.controller;

import com.ducmoba.test_service.domain.dto.ApiResponse;
import com.ducmoba.test_service.domain.dto.request.IntrospectRequest;
import com.ducmoba.test_service.domain.dto.request.LoginRequest;
import com.ducmoba.test_service.domain.dto.request.LogoutRequest;
import com.ducmoba.test_service.domain.dto.request.RefreshRequest;
import com.ducmoba.test_service.domain.dto.response.IntrospectResponse;
import com.ducmoba.test_service.domain.dto.response.LoginResponse;
import com.ducmoba.test_service.service.AuthenticationService;
import com.nimbusds.jose.JOSEException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    @PostMapping("/login")
    ApiResponse<LoginResponse> authenticate(@RequestBody LoginRequest loginRequest){
        var result = authenticationService.authenticate(loginRequest);
        return ApiResponse.<LoginResponse>builder()
                .result(result)
                .build();
    }

    @PostMapping("/introspect")
    ApiResponse<IntrospectResponse> authenticate(@RequestBody IntrospectRequest request) throws ParseException, JOSEException {
        var result = authenticationService.introspect(request);
        return ApiResponse.<IntrospectResponse>builder()
                .result(result)
                .build();
    }

    @PostMapping("/logout")
    ApiResponse<String> logout(@RequestBody LogoutRequest logoutRequest) throws ParseException, JOSEException{
        authenticationService.logout(logoutRequest);
        return ApiResponse.<String>builder()
                .result("logout successful")
                .build();
    }

    @PostMapping("/refresh")
    ApiResponse<LoginResponse> logout(@RequestBody RefreshRequest request) throws ParseException, JOSEException{
        var result = authenticationService.refreshToken(request);
        return ApiResponse.<LoginResponse>builder()
                .result(result)
                .build();
    }

}
