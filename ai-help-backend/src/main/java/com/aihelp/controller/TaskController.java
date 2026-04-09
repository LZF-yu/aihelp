package com.aihelp.controller;

import com.aihelp.common.PageResult;
import com.aihelp.common.Response;
import com.aihelp.dto.request.TaskQueryRequest;
import com.aihelp.dto.request.TaskRequest;
import com.aihelp.dto.response.TaskResponse;
import com.aihelp.service.ITaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final ITaskService taskService;

    @GetMapping
    public Response<PageResult<TaskResponse>> list(
            @AuthenticationPrincipal UserDetails userDetails,
            TaskQueryRequest request) {
        Long userId = Long.parseLong(userDetails.getUsername());
        PageResult<TaskResponse> result = taskService.list(userId, request);
        return Response.success(result);
    }

    @GetMapping("/{id}")
    public Response<TaskResponse> getById(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long id) {
        Long userId = Long.parseLong(userDetails.getUsername());
        TaskResponse response = taskService.getById(userId, id);
        return Response.success(response);
    }

    @PostMapping
    public Response<TaskResponse> create(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody TaskRequest request) {
        Long userId = Long.parseLong(userDetails.getUsername());
        TaskResponse response = taskService.create(userId, request);
        return Response.created(response);
    }

    @PutMapping("/{id}")
    public Response<TaskResponse> update(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long id,
            @RequestBody TaskRequest request) {
        Long userId = Long.parseLong(userDetails.getUsername());
        TaskResponse response = taskService.update(userId, id, request);
        return Response.success(response);
    }

    @DeleteMapping("/{id}")
    public Response<Void> delete(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long id) {
        Long userId = Long.parseLong(userDetails.getUsername());
        taskService.delete(userId, id);
        return Response.success("删除成功", null);
    }

    @DeleteMapping("/batch")
    public Response<Void> batchDelete(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam String ids) {
        Long userId = Long.parseLong(userDetails.getUsername());
        taskService.batchDelete(userId, ids);
        return Response.success("批量删除成功", null);
    }

    @PatchMapping("/{id}/status")
    public Response<Void> updateStatus(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long id,
            @RequestParam Integer status) {
        Long userId = Long.parseLong(userDetails.getUsername());
        taskService.updateStatus(userId, id, status);
        return Response.success("状态更新成功", null);
    }
}
