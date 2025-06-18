package com.ducmoba.test_service.service.serviceImpl;

import com.ducmoba.test_service.domain.dto.request.PermissionRequest;
import com.ducmoba.test_service.domain.dto.response.PermissionResponse;
import com.ducmoba.test_service.domain.entity.Permission;
import com.ducmoba.test_service.mapper.PermissionMapper;
import com.ducmoba.test_service.repository.PermissionRepository;
import com.ducmoba.test_service.service.PermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PermissionServiceImpl implements PermissionService {
    private final PermissionRepository permissionRepository;
    private final PermissionMapper permissionMapper;
    @Override
    public PermissionResponse createPermission(PermissionRequest permissionRequest) {
        Permission permission = permissionMapper.toPermission(permissionRequest);
        permission = permissionRepository.save(permission);
        return permissionMapper.toPermissionResponse(permission);
    }

    @Override
    public List<PermissionResponse> getAll() {
        var permission = permissionRepository.findAll();
        return permission.stream().map(permissionMapper::toPermissionResponse).toList();
    }

    @Override
    public void delete(String permission) {
        permissionRepository.deleteById(permission);
    }

}
