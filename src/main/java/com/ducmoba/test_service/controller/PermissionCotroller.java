package com.ducmoba.test_service.controller;

import com.ducmoba.test_service.domain.dto.ApiResponse;
import com.ducmoba.test_service.domain.dto.request.PermissionRequest;
import com.ducmoba.test_service.domain.dto.response.PermissionResponse;
import com.ducmoba.test_service.service.PermissionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/permission")
public class PermissionCotroller {
    private final PermissionService permissionService;

    @PostMapping
    ApiResponse<PermissionResponse> createPermission(@RequestBody PermissionRequest permissionRequest){
        ApiResponse<PermissionResponse> apiResponse = new ApiResponse<>();
        apiResponse.setResult(permissionService.createPermission(permissionRequest));
        return apiResponse;
    }

    @GetMapping
    ApiResponse<List<PermissionResponse>> getAll(){
        return ApiResponse.<List<PermissionResponse>>builder()
                .result(permissionService.getAll())
                .build();
    }

    @DeleteMapping("/{name}")
    ApiResponse<String> delete(@PathVariable("name") String name){
        permissionService.delete(name);
        return ApiResponse.<String>builder()
                .result("Delete permission successfully")
                .build();
    }


}
