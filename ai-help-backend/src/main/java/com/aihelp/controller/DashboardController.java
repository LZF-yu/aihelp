package com.aihelp.controller;

import com.aihelp.common.Response;
import com.aihelp.dto.response.DashboardResponse;
import com.aihelp.service.IDashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final IDashboardService dashboardService;

    @GetMapping
    public Response<DashboardResponse> getDashboard(
            @AuthenticationPrincipal UserDetails userDetails) {
        Long userId = Long.parseLong(userDetails.getUsername());
        DashboardResponse response = dashboardService.getDashboard(userId);
        return Response.success(response);
    }
}
