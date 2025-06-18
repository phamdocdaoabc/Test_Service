package com.ducmoba.test_service.mapper;

import com.ducmoba.test_service.domain.dto.request.UserCreationRequest;
import com.ducmoba.test_service.domain.dto.request.UserUpdateRequest;
import com.ducmoba.test_service.domain.dto.response.UserResponse;
import com.ducmoba.test_service.domain.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    User toUser(UserCreationRequest request);

    UserResponse toUserResponse(User user);

    @Mapping(target = "roles", ignore = true) // Không cập nhật id để tránh ghi đè dữ liệu
    void updateUserFromRequest(UserUpdateRequest request, @MappingTarget User user);
}
