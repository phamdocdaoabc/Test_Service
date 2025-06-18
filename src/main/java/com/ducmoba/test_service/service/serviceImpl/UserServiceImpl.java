package com.ducmoba.test_service.service.serviceImpl;

import com.ducmoba.test_service.domain.dto.request.UserCreationRequest;
import com.ducmoba.test_service.domain.dto.request.UserUpdateRequest;
import com.ducmoba.test_service.domain.dto.response.UserResponse;
import com.ducmoba.test_service.domain.entity.Roles;
import com.ducmoba.test_service.domain.entity.User;
import com.ducmoba.test_service.domain.enums.Role;
import com.ducmoba.test_service.domain.enums.Status;
import com.ducmoba.test_service.exception.AppException;
import com.ducmoba.test_service.exception.ErrorCode;
import com.ducmoba.test_service.mapper.UserMapper;
import com.ducmoba.test_service.repository.RoleRepository;
import com.ducmoba.test_service.repository.UserRepository;
import com.ducmoba.test_service.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private  final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserResponse createRequest(UserCreationRequest request) {
        log.info("Service: Create User");
        if(userRepository.existsByUserName(request.getUserName())) throw new AppException(ErrorCode.USER_EXISTED);
        User user = UserMapper.INSTANCE.toUser(request);
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        Set<Roles> role = new HashSet<>();
        roleRepository.findById(Role.USER.name()).ifPresent(role::add);
        user.setRoles(role);
        user.setStatus(true);

        try{
            user = userRepository.save(user);
        }catch (DataIntegrityViolationException exception){
            throw new AppException(ErrorCode.USER_EXISTED);
        }
        return UserMapper.INSTANCE.toUserResponse(user);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public List<UserResponse> getUsers() {
        log.info("Method get List User");
        return userRepository.findAll().stream().map(userMapper::toUserResponse).toList();
    }


    @Override
    @PostAuthorize("hasRole('ADMIN')")
    public UserResponse getUserId(String userId) {
        log.info("Method get User");
        return UserMapper.INSTANCE.toUserResponse(userRepository.findById(userId)
                .orElseThrow(()-> new RuntimeException("User not found")));
    }

    @Override
    public UserResponse updateRequest(String userId, UserUpdateRequest userUpdateRequest) {
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new RuntimeException("User not found"));

        // Cập nhật thông tin từ request vào user hiện tại
        UserMapper.INSTANCE.updateUserFromRequest(userUpdateRequest, user);
        user.setPassword(passwordEncoder.encode(userUpdateRequest.getPassword()));
        var roles = roleRepository.findAllById(userUpdateRequest.getRoles());
        user.setRoles(new HashSet<>(roles));
        return UserMapper.INSTANCE.toUserResponse(userRepository.save(user));
    }

    @Override
    public void deleteUser(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new RuntimeException("User not found"));
        userRepository.delete(user);
    }

    @Override
    public void softDeleteUser(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new RuntimeException("User not found"));
        user.setDeleteAt(LocalDateTime.now());
        userRepository.save(user);
    }

    @Override
    public UserResponse getMyInfo() {
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();

        User user = userRepository.findByUserName(name).orElseThrow(()
                -> new AppException(ErrorCode.USER_EXISTED));
        return userMapper.toUserResponse(user);
    }


}
