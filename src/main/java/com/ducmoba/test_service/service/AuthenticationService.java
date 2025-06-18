package com.ducmoba.test_service.service;

import com.ducmoba.test_service.domain.dto.request.IntrospectRequest;
import com.ducmoba.test_service.domain.dto.request.LoginRequest;
import com.ducmoba.test_service.domain.dto.request.LogoutRequest;
import com.ducmoba.test_service.domain.dto.request.RefreshRequest;
import com.ducmoba.test_service.domain.dto.response.IntrospectResponse;
import com.ducmoba.test_service.domain.dto.response.LoginResponse;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jwt.SignedJWT;

import java.text.ParseException;

public interface AuthenticationService {

    LoginResponse authenticate(LoginRequest request);

    IntrospectResponse introspect(IntrospectRequest introspectRequest) throws JOSEException, ParseException;

    void logout(LogoutRequest logoutRequest) throws ParseException, JOSEException;

    SignedJWT verifyToken(String token, boolean isRefresh) throws JOSEException, ParseException;

    LoginResponse refreshToken(RefreshRequest request) throws JOSEException, ParseException;


}
