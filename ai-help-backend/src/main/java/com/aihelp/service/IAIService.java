package com.aihelp.service;

import com.aihelp.dto.request.AIRequest;
import com.aihelp.dto.response.AIResponse;

public interface IAIService {

    AIResponse chat(AIRequest request);

    AIResponse decomposeTask(Long userId, String taskTitle, String taskDescription);

    AIResponse sortPriority(Long userId);

    AIResponse generateDailyPlan(Long userId);

    AIResponse generateWeeklyPlan(Long userId);

    AIResponse generateDailyReport(Long userId);

    AIResponse generateWeeklyReport(Long userId);
}
