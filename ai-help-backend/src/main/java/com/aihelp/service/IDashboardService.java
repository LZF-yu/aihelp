package com.aihelp.service;

import com.aihelp.dto.response.DashboardResponse;

public interface IDashboardService {

    DashboardResponse getDashboard(Long userId);
}
