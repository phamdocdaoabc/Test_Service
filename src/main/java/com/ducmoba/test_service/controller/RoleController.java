package com.ducmoba.test_service.controller;

import com.ducmoba.test_service.domain.dto.ApiResponse;
import com.ducmoba.test_service.domain.dto.request.RoleRequest;
import com.ducmoba.test_service.domain.dto.request.RoleUpdateRequest;
import com.ducmoba.test_service.domain.dto.response.RoleResponse;
import com.ducmoba.test_service.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/role")
@RequiredArgsConstructor
public class RoleController {
    private final RoleService roleService;

    @PostMapping
    ApiResponse<RoleResponse> createRole(@RequestBody RoleRequest roleRequest){
        ApiResponse<RoleResponse> apiResponse = new ApiResponse<>();
        apiResponse.setResult(roleService.createRole(roleRequest));
        return apiResponse;
    }

    @GetMapping
    ApiResponse<List<RoleResponse>> getAll(){
        return ApiResponse.<List<RoleResponse>>builder()
                .result(roleService.getAll())
                .build();
    }

    @PutMapping()
    ApiResponse<RoleResponse> updateRole(@RequestParam String name, @RequestBody RoleUpdateRequest roleUpdateRequest){
        return ApiResponse.<RoleResponse>builder()
                .result(roleService.updateRole(name, roleUpdateRequest))
                .build();
    }


    @DeleteMapping("/{name}")
    ApiResponse<String> deleteRole(@PathVariable("name") String name){
        roleService.delete(name);
        return ApiResponse.<String>builder()
                .result("Delete Role Scuccessfully")
                .build();
    }

}
