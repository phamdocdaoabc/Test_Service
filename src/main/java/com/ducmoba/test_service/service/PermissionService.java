package com.ducmoba.test_service.service;

import com.ducmoba.test_service.domain.dto.request.PermissionRequest;
import com.ducmoba.test_service.domain.dto.response.PermissionResponse;

import java.util.List;

public interface PermissionService {
    PermissionResponse createPermission(PermissionRequest permissionRequest);

    List<PermissionResponse> getAll();

    void delete(String permission);
}
