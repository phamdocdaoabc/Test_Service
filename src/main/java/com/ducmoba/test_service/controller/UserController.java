package com.ducmoba.test_service.controller;

import com.ducmoba.test_service.domain.dto.ApiResponse;
import com.ducmoba.test_service.domain.dto.request.UserCreationRequest;
import com.ducmoba.test_service.domain.dto.request.UserUpdateRequest;
import com.ducmoba.test_service.domain.dto.response.UserResponse;
import com.ducmoba.test_service.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;

    @PostMapping()
    ApiResponse<UserResponse> createUser(@RequestBody @Valid UserCreationRequest userCreationRequest){
        log.info("Controller: Create User");
       ApiResponse<UserResponse> apiResponse = new ApiResponse<>();
       apiResponse.setResult(userService.createRequest(userCreationRequest));
       return apiResponse;
    }

    @GetMapping()
    ApiResponse<List<UserResponse>> getUsers(){
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info("UserName: {}", authentication.getName());
        authentication.getAuthorities().forEach(grantedAuthority -> log.info(grantedAuthority.getAuthority()));
        return ApiResponse.<List<UserResponse>>builder()
                .result(userService.getUsers())
                .build();
    }

    @GetMapping("/{userId}")
    UserResponse getUser(@PathVariable("userId") String userId){
        return userService.getUserId(userId);
    }

    @GetMapping("/my_info")
    ApiResponse<UserResponse> getMyInfo(){
        return ApiResponse.<UserResponse>builder()
                .result(userService.getMyInfo())
                .build();
    }

    @PutMapping("/{userId}")
    UserResponse updateUser(@PathVariable String userId, @RequestBody UserUpdateRequest userUpdateRequest){
        return userService.updateRequest(userId, userUpdateRequest);
    }

    @DeleteMapping("/{userId}")
    String deleteUser(@PathVariable String userId){
        userService.deleteUser(userId);
        return "User has been deleted";
    }

    @DeleteMapping("/soft_delete/{userId}")
    String sortDeleteUser(@PathVariable String userId){
        userService.softDeleteUser(userId);
        return "User has been deleted";
    }

}
