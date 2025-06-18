package com.ducmoba.test_service.service;

import com.ducmoba.test_service.domain.dto.request.RoleRequest;
import com.ducmoba.test_service.domain.dto.request.RoleUpdateRequest;
import com.ducmoba.test_service.domain.dto.response.RoleResponse;

import java.util.List;

public interface RoleService {

    RoleResponse createRole(RoleRequest roleRequest);

    List<RoleResponse> getAll();

    void delete(String name);

    RoleResponse updateRole(String Id, RoleUpdateRequest roleUpdateRequest);
}
