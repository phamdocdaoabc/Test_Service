package com.ducmoba.test_service.service;

import com.ducmoba.test_service.domain.dto.request.UserCreationRequest;
import com.ducmoba.test_service.domain.dto.request.UserUpdateRequest;
import com.ducmoba.test_service.domain.dto.response.UserResponse;

import java.util.List;

public interface UserService {
    UserResponse createRequest(UserCreationRequest request);

    List<UserResponse> getUsers();

    UserResponse getUserId(String userId);

    UserResponse updateRequest(String userId, UserUpdateRequest userUpdateRequest);

    void deleteUser(String userId);

    void softDeleteUser(String userId);

    UserResponse getMyInfo();

}
