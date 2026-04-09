package com.aihelp.controller;

import com.aihelp.common.Response;
import com.aihelp.dto.request.LoginRequest;
import com.aihelp.dto.request.PasswordUpdateRequest;
import com.aihelp.dto.request.RegisterRequest;
import com.aihelp.dto.request.UserUpdateRequest;
import com.aihelp.dto.response.LoginResponse;
import com.aihelp.dto.response.UserResponse;
import com.aihelp.service.IUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final IUserService userService;

    @PostMapping("/register")
    public Response<LoginResponse> register(@Valid @RequestBody RegisterRequest request) {
        LoginResponse response = userService.register(request);
        return Response.created(response);
    }

    @PostMapping("/login")
    public Response<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        LoginResponse response = userService.login(request);
        return Response.success(response);
    }

    @GetMapping("/me")
    public Response<UserResponse> getCurrentUser(@AuthenticationPrincipal UserDetails userDetails) {
        Long userId = Long.parseLong(userDetails.getUsername());
        UserResponse response = userService.getCurrentUser(userId);
        return Response.success(response);
    }

    @PutMapping("/me")
    public Response<Void> updateUser(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody UserUpdateRequest request) {
        Long userId = Long.parseLong(userDetails.getUsername());
        userService.updateUser(userId, request);
        return Response.success("修改成功", null);
    }

    @PutMapping("/password")
    public Response<Void> updatePassword(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody PasswordUpdateRequest request) {
        Long userId = Long.parseLong(userDetails.getUsername());
        userService.updatePassword(userId, request);
        return Response.success("密码修改成功", null);
    }
}
