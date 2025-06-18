package com.ducmoba.test_service.service.serviceImpl;

import com.ducmoba.test_service.domain.dto.request.RoleRequest;
import com.ducmoba.test_service.domain.dto.request.RoleUpdateRequest;
import com.ducmoba.test_service.domain.dto.response.RoleResponse;
import com.ducmoba.test_service.domain.entity.Roles;
import com.ducmoba.test_service.mapper.RoleMapper;
import com.ducmoba.test_service.repository.PermissionRepository;
import com.ducmoba.test_service.repository.RoleRepository;
import com.ducmoba.test_service.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;
    private final PermissionRepository permissionRepository;

    @Override
    public RoleResponse createRole(RoleRequest roleRequest) {
        Roles role = roleMapper.toRole(roleRequest);
        var permissions = permissionRepository.findAllById(roleRequest.getPermissions());
        role.setPermissions(new HashSet<>(permissions));
        role = roleRepository.save(role);
        return roleMapper.toRoleResponse(role);
    }

    @Override
    public List<RoleResponse> getAll() {
        var roles = roleRepository.findAll();
        return roles.stream().map(roleMapper::toRoleResponse).toList();
    }

    @Override
    public void delete(String name) {
        roleRepository.deleteById(name);
    }

    @Override
    public RoleResponse updateRole(String Id, RoleUpdateRequest roleUpdateRequest) {
        Roles roles = roleRepository.findById(Id) .orElseThrow(()-> new RuntimeException("Role not found"));
        roles.setDescription(roleUpdateRequest.getDescription());
        var permissions = permissionRepository.findAllById(roleUpdateRequest.getPermissions());
        roles.setPermissions(new HashSet<>(permissions));
        roles = roleRepository.save(roles);
        return roleMapper.toRoleResponse(roles);
    }
}
