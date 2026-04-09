package com.aihelp.service;

import com.aihelp.dto.request.TaskQueryRequest;
import com.aihelp.dto.request.TaskRequest;
import com.aihelp.dto.response.TaskResponse;
import com.aihelp.common.PageResult;

public interface ITaskService {

    TaskResponse create(Long userId, TaskRequest request);

    TaskResponse update(Long userId, Long taskId, TaskRequest request);

    void delete(Long userId, Long taskId);

    void batchDelete(Long userId, String ids);

    TaskResponse getById(Long userId, Long taskId);

    PageResult<TaskResponse> list(Long userId, TaskQueryRequest request);

    void updateStatus(Long userId, Long taskId, Integer status);
}
