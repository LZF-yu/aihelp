package com.aihelp.controller;

import com.aihelp.common.Response;
import com.aihelp.dto.request.AIRequest;
import com.aihelp.dto.response.AIResponse;
import com.aihelp.service.IAIService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class AIController {

    private final IAIService aiService;

    @PostMapping("/chat")
    public Response<AIResponse> chat(@Valid @RequestBody AIRequest request) {
        AIResponse response = aiService.chat(request);
        return Response.success(response);
    }

    @PostMapping("/decompose")
    public Response<AIResponse> decomposeTask(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam String taskTitle,
            @RequestParam(required = false) String taskDescription) {
        Long userId = Long.parseLong(userDetails.getUsername());
        AIResponse response = aiService.decomposeTask(userId, taskTitle, taskDescription);
        return Response.success(response);
    }

    @PostMapping("/priority/sort")
    public Response<AIResponse> sortPriority(
            @AuthenticationPrincipal UserDetails userDetails) {
        Long userId = Long.parseLong(userDetails.getUsername());
        AIResponse response = aiService.sortPriority(userId);
        return Response.success(response);
    }

    @PostMapping("/plan/daily")
    public Response<AIResponse> generateDailyPlan(
            @AuthenticationPrincipal UserDetails userDetails) {
        Long userId = Long.parseLong(userDetails.getUsername());
        AIResponse response = aiService.generateDailyPlan(userId);
        return Response.success(response);
    }

    @PostMapping("/plan/weekly")
    public Response<AIResponse> generateWeeklyPlan(
            @AuthenticationPrincipal UserDetails userDetails) {
        Long userId = Long.parseLong(userDetails.getUsername());
        AIResponse response = aiService.generateWeeklyPlan(userId);
        return Response.success(response);
    }

    @PostMapping("/report/daily")
    public Response<AIResponse> generateDailyReport(
            @AuthenticationPrincipal UserDetails userDetails) {
        Long userId = Long.parseLong(userDetails.getUsername());
        AIResponse response = aiService.generateDailyReport(userId);
        return Response.success(response);
    }

    @PostMapping("/report/weekly")
    public Response<AIResponse> generateWeeklyReport(
            @AuthenticationPrincipal UserDetails userDetails) {
        Long userId = Long.parseLong(userDetails.getUsername());
        AIResponse response = aiService.generateWeeklyReport(userId);
        return Response.success(response);
    }
}
