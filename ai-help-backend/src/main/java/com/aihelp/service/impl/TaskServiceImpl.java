package com.aihelp.service.impl;

import com.aihelp.dto.request.TaskQueryRequest;
import com.aihelp.dto.request.TaskRequest;
import com.aihelp.dto.response.TaskResponse;
import com.aihelp.entity.Category;
import com.aihelp.entity.Task;
import com.aihelp.mapper.CategoryMapper;
import com.aihelp.mapper.TaskMapper;
import com.aihelp.service.ITaskService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements ITaskService {

    private final TaskMapper taskMapper;
    private final CategoryMapper categoryMapper;

    @Override
    public TaskResponse create(Long userId, TaskRequest request) {
        Task task = new Task();
        task.setUserId(userId);
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setCategoryId(request.getCategoryId());
        task.setParentId(request.getParentId());
        task.setPriority(request.getPriority() != null ? request.getPriority() : "middle");
        task.setStatus(0);
        task.setDeadline(request.getDeadline());
        task.setReminderTime(request.getReminderTime());
        task.setTags(request.getTags());

        taskMapper.insert(task);

        return toResponse(task);
    }

    @Override
    public TaskResponse update(Long userId, Long taskId, TaskRequest request) {
        Task task = taskMapper.selectById(taskId);
        if (task == null || !task.getUserId().equals(userId)) {
            throw new IllegalArgumentException("任务不存在或无权限修改");
        }

        if (StringUtils.hasText(request.getTitle())) {
            task.setTitle(request.getTitle());
        }
        if (request.getDescription() != null) {
            task.setDescription(request.getDescription());
        }
        if (request.getCategoryId() != null) {
            task.setCategoryId(request.getCategoryId());
        }
        if (StringUtils.hasText(request.getPriority())) {
            task.setPriority(request.getPriority());
        }
        if (request.getStatus() != null) {
            task.setStatus(request.getStatus());
            if (request.getStatus() == 1) {
                task.setCompletedTime(LocalDateTime.now());
            }
        }
        if (request.getDeadline() != null) {
            task.setDeadline(request.getDeadline());
        }
        if (request.getReminderTime() != null) {
            task.setReminderTime(request.getReminderTime());
        }
        if (request.getTags() != null) {
            task.setTags(request.getTags());
        }

        taskMapper.updateById(task);

        return toResponse(task);
    }

    @Override
    public void delete(Long userId, Long taskId) {
        Task task = taskMapper.selectById(taskId);
        if (task == null || !task.getUserId().equals(userId)) {
            throw new IllegalArgumentException("任务不存在或无权限删除");
        }

        taskMapper.deleteById(taskId);
    }

    @Override
    public void batchDelete(Long userId, String ids) {
        List<Long> idList = Arrays.stream(ids.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .map(Long::parseLong)
                .collect(Collectors.toList());

        if (!idList.isEmpty()) {
            taskMapper.delete(new LambdaQueryWrapper<Task>()
                    .eq(Task::getUserId, userId)
                    .in(Task::getId, idList));
        }
    }

    @Override
    public TaskResponse getById(Long userId, Long taskId) {
        Task task = taskMapper.selectById(taskId);
        if (task == null || !task.getUserId().equals(userId)) {
            throw new IllegalArgumentException("任务不存在");
        }

        return toResponse(task);
    }

    @Override
    public com.aihelp.common.PageResult<TaskResponse> list(Long userId, TaskQueryRequest request) {
        LambdaQueryWrapper<Task> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Task::getUserId, userId);

        if (request.getCategoryId() != null) {
            wrapper.eq(Task::getCategoryId, request.getCategoryId());
        }
        if (request.getStatus() != null) {
            wrapper.eq(Task::getStatus, request.getStatus());
        }
        if (StringUtils.hasText(request.getPriority())) {
            wrapper.eq(Task::getPriority, request.getPriority());
        }
        if (request.getParentId() != null) {
            wrapper.eq(Task::getParentId, request.getParentId());
        } else {
            wrapper.isNull(Task::getParentId);
        }
        if (StringUtils.hasText(request.getKeyword())) {
            wrapper.and(w -> w.like(Task::getTitle, request.getKeyword())
                    .or().like(Task::getDescription, request.getKeyword()));
        }

        wrapper.orderByDesc(Task::getCreateTime);

        Page<Task> page = new Page<>(request.getCurrent(), request.getSize());
        IPage<Task> pageResult = taskMapper.selectPage(page, wrapper);

        List<TaskResponse> records = pageResult.getRecords().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());

        return com.aihelp.common.PageResult.of(
                pageResult.getTotal(),
                records,
                pageResult.getCurrent(),
                pageResult.getSize()
        );
    }

    @Override
    public void updateStatus(Long userId, Long taskId, Integer status) {
        Task task = taskMapper.selectById(taskId);
        if (task == null || !task.getUserId().equals(userId)) {
            throw new IllegalArgumentException("任务不存在或无权限修改");
        }

        task.setStatus(status);
        if (status == 1) {
            task.setCompletedTime(LocalDateTime.now());
        } else {
            task.setCompletedTime(null);
        }

        taskMapper.updateById(task);
    }

    private TaskResponse toResponse(Task task) {
        TaskResponse.TaskResponseBuilder builder = TaskResponse.builder()
                .id(task.getId())
                .userId(task.getUserId())
                .categoryId(task.getCategoryId())
                .parentId(task.getParentId())
                .title(task.getTitle())
                .description(task.getDescription())
                .priority(task.getPriority())
                .status(task.getStatus())
                .deadline(task.getDeadline())
                .reminderTime(task.getReminderTime())
                .completedTime(task.getCompletedTime())
                .tags(task.getTags())
                .createTime(task.getCreateTime())
                .updateTime(task.getUpdateTime());

        if (task.getCategoryId() != null) {
            Category category = categoryMapper.selectById(task.getCategoryId());
            if (category != null) {
                builder.categoryName(category.getCategoryName())
                        .categoryColor(category.getCategoryColor());
            }
        }

        return builder.build();
    }
}
