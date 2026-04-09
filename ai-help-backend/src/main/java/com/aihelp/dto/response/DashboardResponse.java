package com.aihelp.dto.response;

import lombok.Data;
import java.util.Map;
import java.util.List;

@Data
public class DashboardResponse {
    private long todayTotal;
    private long todayCompleted;
    private long pendingTotal;
    private long completedTotal;
    private Map<String, Long> statusStats;
    private Map<String, Long> categoryStats;
    private List<TaskResponse> todayPriorityTasks;
}
