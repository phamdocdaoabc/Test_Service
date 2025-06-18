package com.ducmoba.test_service.mapper;

import com.ducmoba.test_service.domain.dto.request.PermissionRequest;
import com.ducmoba.test_service.domain.dto.response.PermissionResponse;
import com.ducmoba.test_service.domain.entity.Permission;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
    PermissionMapper INSTANCE = Mappers.getMapper(PermissionMapper.class);

    Permission toPermission(PermissionRequest permissionRequest);
    PermissionResponse toPermissionResponse(Permission permission);
}
