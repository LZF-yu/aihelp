package com.aihelp.service.impl;

import com.aihelp.dto.request.CategoryRequest;
import com.aihelp.dto.response.CategoryResponse;
import com.aihelp.entity.Category;
import com.aihelp.mapper.CategoryMapper;
import com.aihelp.service.ICategoryService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements ICategoryService {

    private final CategoryMapper categoryMapper;

    @Override
    public List<CategoryResponse> listByUser(Long userId) {
        LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Category::getUserId, userId)
                .orderByAsc(Category::getSortOrder)
                .orderByDesc(Category::getCreateTime);

        List<Category> categories = categoryMapper.selectList(wrapper);

        return categories.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryResponse create(Long userId, CategoryRequest request) {
        Category category = new Category();
        category.setUserId(userId);
        category.setCategoryName(request.getCategoryName());
        category.setCategoryColor(request.getCategoryColor() != null ?
                request.getCategoryColor() : "#409EFF");
        category.setSortOrder(request.getSortOrder() != null ?
                request.getSortOrder() : 0);

        categoryMapper.insert(category);

        return toResponse(category);
    }

    @Override
    public CategoryResponse update(Long userId, Long categoryId, CategoryRequest request) {
        Category category = categoryMapper.selectById(categoryId);
        if (category == null || !category.getUserId().equals(userId)) {
            throw new IllegalArgumentException("分类不存在或无权限修改");
        }

        if (request.getCategoryName() != null) {
            category.setCategoryName(request.getCategoryName());
        }
        if (request.getCategoryColor() != null) {
            category.setCategoryColor(request.getCategoryColor());
        }
        if (request.getSortOrder() != null) {
            category.setSortOrder(request.getSortOrder());
        }

        categoryMapper.updateById(category);

        return toResponse(category);
    }

    @Override
    public void delete(Long userId, Long categoryId) {
        Category category = categoryMapper.selectById(categoryId);
        if (category == null || !category.getUserId().equals(userId)) {
            throw new IllegalArgumentException("分类不存在或无权限删除");
        }

        categoryMapper.deleteById(categoryId);
    }

    private CategoryResponse toResponse(Category category) {
        return CategoryResponse.builder()
                .id(category.getId())
                .userId(category.getUserId())
                .categoryName(category.getCategoryName())
                .categoryColor(category.getCategoryColor())
                .sortOrder(category.getSortOrder())
                .createTime(category.getCreateTime())
                .build();
    }
}
