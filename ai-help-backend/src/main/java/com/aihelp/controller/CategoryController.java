package com.aihelp.controller;

import com.aihelp.common.Response;
import com.aihelp.dto.request.CategoryRequest;
import com.aihelp.dto.response.CategoryResponse;
import com.aihelp.service.ICategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final ICategoryService categoryService;

    @GetMapping
    public Response<List<CategoryResponse>> list(@AuthenticationPrincipal UserDetails userDetails) {
        Long userId = Long.parseLong(userDetails.getUsername());
        List<CategoryResponse> list = categoryService.listByUser(userId);
        return Response.success(list);
    }

    @PostMapping
    public Response<CategoryResponse> create(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody CategoryRequest request) {
        Long userId = Long.parseLong(userDetails.getUsername());
        CategoryResponse response = categoryService.create(userId, request);
        return Response.created(response);
    }

    @PutMapping("/{id}")
    public Response<CategoryResponse> update(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long id,
            @RequestBody CategoryRequest request) {
        Long userId = Long.parseLong(userDetails.getUsername());
        CategoryResponse response = categoryService.update(userId, id, request);
        return Response.success(response);
    }

    @DeleteMapping("/{id}")
    public Response<Void> delete(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long id) {
        Long userId = Long.parseLong(userDetails.getUsername());
        categoryService.delete(userId, id);
        return Response.success("删除成功", null);
    }
}
