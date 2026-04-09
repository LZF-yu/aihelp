package com.aihelp.service.impl;

import com.aihelp.common.ResultCode;
import com.aihelp.dto.request.LoginRequest;
import com.aihelp.dto.request.PasswordUpdateRequest;
import com.aihelp.dto.request.RegisterRequest;
import com.aihelp.dto.request.UserUpdateRequest;
import com.aihelp.dto.response.LoginResponse;
import com.aihelp.dto.response.UserResponse;
import com.aihelp.entity.Category;
import com.aihelp.entity.User;
import com.aihelp.mapper.CategoryMapper;
import com.aihelp.mapper.UserMapper;
import com.aihelp.security.JwtUtil;
import com.aihelp.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService {

    private final UserMapper userMapper;
    private final CategoryMapper categoryMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Override
    @Transactional
    public LoginResponse register(RegisterRequest request) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, request.getUsername());
        if (userMapper.selectCount(wrapper).intValue() > 0) {
            throw new IllegalArgumentException("用户名已存在");
        }

        if (request.getEmail() != null && !request.getEmail().isEmpty()) {
            LambdaQueryWrapper<User> emailWrapper = new LambdaQueryWrapper<>();
            emailWrapper.eq(User::getEmail, request.getEmail());
            if (userMapper.selectCount(emailWrapper).intValue() > 0) {
                throw new IllegalArgumentException("邮箱已被注册");
            }
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEmail(request.getEmail());
        user.setNickname(request.getUsername());
        user.setStatus(1);

        userMapper.insert(user);

        createDefaultCategories(user.getId());

        String token = jwtUtil.generateToken(user.getId(), user.getUsername());

        return LoginResponse.builder()
                .token(token)
                .username(user.getUsername())
                .userId(user.getId())
                .build();
    }

    private void createDefaultCategories(Long userId) {
        String[][] defaultCategories = {
            {"工作", "#F56C6C"},
            {"学习", "#409EFF"},
            {"生活", "#67C23A"},
            {"其他", "#909399"}
        };
        for (int i = 0; i < defaultCategories.length; i++) {
            Category category = new Category();
            category.setUserId(userId);
            category.setCategoryName(defaultCategories[i][0]);
            category.setCategoryColor(defaultCategories[i][1]);
            category.setSortOrder(i + 1);
            categoryMapper.insert(category);
        }
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, request.getUsername());
        User user = userMapper.selectOne(wrapper);

        if (user == null) {
            throw new IllegalArgumentException("用户不存在");
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("密码错误");
        }

        if (user.getStatus() != 1) {
            throw new IllegalArgumentException("账号已被禁用");
        }

        String token = jwtUtil.generateToken(user.getId(), user.getUsername());

        return LoginResponse.builder()
                .token(token)
                .username(user.getUsername())
                .userId(user.getId())
                .build();
    }

    @Override
    public UserResponse getCurrentUser(Long userId) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new IllegalArgumentException("用户不存在");
        }

        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .avatar(user.getAvatar())
                .createTime(user.getCreateTime())
                .build();
    }

    @Override
    public void updateUser(Long userId, UserUpdateRequest request) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new IllegalArgumentException("用户不存在");
        }

        if (request.getNickname() != null) {
            user.setNickname(request.getNickname());
        }
        if (request.getEmail() != null) {
            user.setEmail(request.getEmail());
        }
        if (request.getAvatar() != null) {
            user.setAvatar(request.getAvatar());
        }

        userMapper.updateById(user);
    }

    @Override
    public void updatePassword(Long userId, PasswordUpdateRequest request) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new IllegalArgumentException("用户不存在");
        }

        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new IllegalArgumentException("旧密码错误");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userMapper.updateById(user);
    }
}
