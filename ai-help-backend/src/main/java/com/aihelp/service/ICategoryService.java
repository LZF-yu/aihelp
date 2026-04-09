package com.aihelp.service;

import com.aihelp.dto.request.CategoryRequest;
import com.aihelp.dto.response.CategoryResponse;
import java.util.List;

public interface ICategoryService {

    List<CategoryResponse> listByUser(Long userId);

    CategoryResponse create(Long userId, CategoryRequest request);

    CategoryResponse update(Long userId, Long categoryId, CategoryRequest request);

    void delete(Long userId, Long categoryId);
}
