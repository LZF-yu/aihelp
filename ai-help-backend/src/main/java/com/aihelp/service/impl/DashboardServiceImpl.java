package com.aihelp.service.impl;

import com.aihelp.dto.response.DashboardResponse;
import com.aihelp.dto.response.TaskResponse;
import com.aihelp.entity.Category;
import com.aihelp.entity.Task;
import com.aihelp.mapper.CategoryMapper;
import com.aihelp.mapper.TaskMapper;
import com.aihelp.service.IDashboardService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements IDashboardService {

    private final TaskMapper taskMapper;
    private final CategoryMapper categoryMapper;

    @Override
    public DashboardResponse getDashboard(Long userId) {
        DashboardResponse response = new DashboardResponse();

        LocalDateTime todayStart = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        LocalDateTime todayEnd = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);

        LambdaQueryWrapper<Task> todayWrapper = new LambdaQueryWrapper<>();
        todayWrapper.eq(Task::getUserId, userId)
                .ge(Task::getCreateTime, todayStart)
                .le(Task::getCreateTime, todayEnd);
        long todayTotal = taskMapper.selectCount(todayWrapper);

        LambdaQueryWrapper<Task> todayCompletedWrapper = new LambdaQueryWrapper<>();
        todayCompletedWrapper.eq(Task::getUserId, userId)
                .eq(Task::getStatus, 1)
                .ge(Task::getCompletedTime, todayStart)
                .le(Task::getCompletedTime, todayEnd);
        long todayCompleted = taskMapper.selectCount(todayCompletedWrapper);

        LambdaQueryWrapper<Task> pendingWrapper = new LambdaQueryWrapper<>();
        pendingWrapper.eq(Task::getUserId, userId).eq(Task::getStatus, 0);
        long pendingTotal = taskMapper.selectCount(pendingWrapper);

        LambdaQueryWrapper<Task> completedWrapper = new LambdaQueryWrapper<>();
        completedWrapper.eq(Task::getUserId, userId).eq(Task::getStatus, 1);
        long completedTotal = taskMapper.selectCount(completedWrapper);

        Map<String, Long> statusStats = new HashMap<>();
        statusStats.put("pending", pendingTotal);
        statusStats.put("completed", completedTotal);

        List<Category> categories = categoryMapper.selectList(
                new LambdaQueryWrapper<Category>().eq(Category::getUserId, userId)
        );

        Map<String, Long> categoryStats = new HashMap<>();
        for (Category category : categories) {
            LambdaQueryWrapper<Task> catWrapper = new LambdaQueryWrapper<>();
            catWrapper.eq(Task::getUserId, userId)
                    .eq(Task::getCategoryId, category.getId());
            long count = taskMapper.selectCount(catWrapper);
            categoryStats.put(category.getCategoryName(), count);
        }

        LambdaQueryWrapper<Task> priorityWrapper = new LambdaQueryWrapper<>();
        priorityWrapper.eq(Task::getUserId, userId)
                .eq(Task::getStatus, 0)
                .isNull(Task::getParentId)
                .orderByAsc(Task::getDeadline)
                .last("LIMIT 5");
        List<Task> priorityTasks = taskMapper.selectList(priorityWrapper);

        List<TaskResponse> todayPriorityTasks = priorityTasks.stream()
                .map(this::toTaskResponse)
                .collect(Collectors.toList());

        response.setTodayTotal(todayTotal);
        response.setTodayCompleted(todayCompleted);
        response.setPendingTotal(pendingTotal);
        response.setCompletedTotal(completedTotal);
        response.setStatusStats(statusStats);
        response.setCategoryStats(categoryStats);
        response.setTodayPriorityTasks(todayPriorityTasks);

        return response;
    }

    private TaskResponse toTaskResponse(Task task) {
        return TaskResponse.builder()
                .id(task.getId())
                .title(task.getTitle())
                .priority(task.getPriority())
                .status(task.getStatus())
                .deadline(task.getDeadline())
                .createTime(task.getCreateTime())
                .build();
    }
}
