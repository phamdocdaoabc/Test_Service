package com.ducmoba.test_service.mapper;

import com.ducmoba.test_service.domain.dto.request.RoleRequest;

import com.ducmoba.test_service.domain.dto.response.RoleResponse;

import com.ducmoba.test_service.domain.entity.Roles;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    RoleMapper INSTANCE = Mappers.getMapper(RoleMapper.class);

    @Mapping(target = "permissions", ignore = true)
    Roles toRole(RoleRequest roleRequest);

    @Mappings({
            @Mapping(source = "permissions", target = "permissions")
    })
    RoleResponse toRoleResponse(Roles roles);
}
