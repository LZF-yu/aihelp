package com.aihelp.service;

import com.aihelp.dto.request.LoginRequest;
import com.aihelp.dto.request.PasswordUpdateRequest;
import com.aihelp.dto.request.RegisterRequest;
import com.aihelp.dto.request.UserUpdateRequest;
import com.aihelp.dto.response.LoginResponse;
import com.aihelp.dto.response.UserResponse;

public interface IUserService {

    LoginResponse register(RegisterRequest request);

    LoginResponse login(LoginRequest request);

    UserResponse getCurrentUser(Long userId);

    void updateUser(Long userId, UserUpdateRequest request);

    void updatePassword(Long userId, PasswordUpdateRequest request);
}
