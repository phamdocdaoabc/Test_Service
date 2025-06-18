package com.ducmoba.test_service.service.serviceImpl;

import com.ducmoba.test_service.domain.dto.request.IntrospectRequest;
import com.ducmoba.test_service.domain.dto.request.LoginRequest;
import com.ducmoba.test_service.domain.dto.request.LogoutRequest;
import com.ducmoba.test_service.domain.dto.request.RefreshRequest;
import com.ducmoba.test_service.domain.dto.response.IntrospectResponse;
import com.ducmoba.test_service.domain.dto.response.LoginResponse;
import com.ducmoba.test_service.domain.entity.InvalidatedToken;
import com.ducmoba.test_service.domain.entity.User;
import com.ducmoba.test_service.domain.enums.Status;
import com.ducmoba.test_service.exception.AppException;
import com.ducmoba.test_service.exception.ErrorCode;
import com.ducmoba.test_service.repository.InvalidatedTokenRepository;
import com.ducmoba.test_service.repository.UserRepository;
import com.ducmoba.test_service.service.AuthenticationService;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserRepository userRepository;
    private final InvalidatedTokenRepository invalidatedTokenRepository;

    @NonFinal
    @Value("${jwt.signerKey}")
    protected String SIGNER_KEY;

    @NonFinal
    @Value("${jwt.valid-duration}")
    protected long EXPIRATION;

    @NonFinal
    @Value("${jwt.refreshable-duration}")
    protected long REFRESH_EXPIRATION;

    @Override
    public LoginResponse authenticate(LoginRequest request) {
        User user = userRepository.findByUserName(request.getUserName())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        boolean isLogin = passwordEncoder.matches(request.getPassword(), user.getPassword());

        if(!isLogin){
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
        var token = generateToken(user);
        return  LoginResponse.builder()
                .token(token)
                .islogin(true)
                .build();
    }

    @Override
    public IntrospectResponse introspect(IntrospectRequest introspectRequest) throws JOSEException, ParseException {
        var token = introspectRequest.getToken();
        boolean valid = true;
        try {
            verifyToken(token, false);
        }catch (AppException e){
            valid = false;
        }
        return IntrospectResponse.builder()
                .valid(valid)
                .build();
    }


    @Override
    public void logout(LogoutRequest logoutRequest) throws ParseException, JOSEException {
        try {
            var signedJWT = verifyToken(logoutRequest.getToken(), true);
            String jwt = signedJWT.getJWTClaimsSet().getJWTID();
            Date expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();
            InvalidatedToken invalidatedToken = InvalidatedToken.builder()
                    .id(jwt)
                    .expiryTime(expiryTime)
                    .build();

            invalidatedTokenRepository.save( invalidatedToken);
        }catch (AppException e){
            log.info("Token already expired");
        }
    }

    @Override
    public SignedJWT verifyToken(String token, boolean isRefresh) throws JOSEException, ParseException {
        JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());
        SignedJWT signedJWT = SignedJWT.parse(token);
        // Chía làm verify khi refresh token và khi authentiocation
        Date expiryTime = (isRefresh)
               ? new Date(signedJWT.getJWTClaimsSet().getExpirationTime().toInstant().plus(REFRESH_EXPIRATION, ChronoUnit.SECONDS).toEpochMilli())
                :signedJWT.getJWTClaimsSet().getExpirationTime();
        var verified = signedJWT.verify(verifier);
        if(!(verified && expiryTime.after(new Date()))){
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }

        if(invalidatedTokenRepository.existsById(signedJWT.getJWTClaimsSet().getJWTID())){
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
        return signedJWT;
    }

    @Override
    public LoginResponse refreshToken(RefreshRequest request) throws JOSEException, ParseException{
        var signedJWT = verifyToken(request.getToken(), true);
        var jti = signedJWT.getJWTClaimsSet().getJWTID();
        Date expityTime = signedJWT.getJWTClaimsSet().getExpirationTime();
        InvalidatedToken invalidatedToken = InvalidatedToken.builder()
                .id(jti)
                .expiryTime(expityTime)
                .build();

        invalidatedTokenRepository.save( invalidatedToken);

        invalidatedTokenRepository.save( invalidatedToken);
        var userName = signedJWT.getJWTClaimsSet().getSubject();
        var user = userRepository.findByUserName(userName).orElseThrow(() -> new AppException(ErrorCode.UNAUTHENTICATED) );
        var token = generateToken(user);
        return  LoginResponse.builder()
                .token(token)
                .islogin(true)
                .build();

    }

    private String generateToken(User user){
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getUserName())
                .issuer("DucIT")
                .issueTime(new Date())
                .expirationTime(new Date(Instant.now().plus(EXPIRATION, ChronoUnit.SECONDS).toEpochMilli()))
                .jwtID(UUID.randomUUID().toString())
                .claim("scope", buildScope(user))
                .build();

        JWSObject jwsObject = new JWSObject(new JWSHeader(JWSAlgorithm.HS512), new Payload(jwtClaimsSet.toJSONObject()));
        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        }
    }

     private String buildScope(User user){
         StringJoiner stringJoiner = new StringJoiner(" ");
         if(!CollectionUtils.isEmpty(user.getRoles())){
            user.getRoles().forEach(roles -> {
                stringJoiner.add("ROLE_" + roles.getName());
                if(!CollectionUtils.isEmpty(roles.getPermissions())){
                    roles.getPermissions().forEach(permission -> stringJoiner.add(permission.getName()));
                }
            });
         }
         return stringJoiner.toString();
     }

}
