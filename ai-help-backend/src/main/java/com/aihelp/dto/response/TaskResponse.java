package com.aihelp.dto.response;

import lombok.Data;
import lombok.Builder;
import java.time.LocalDateTime;

@Data
@Builder
public class TaskResponse {
    private Long id;
    private Long userId;
    private Long categoryId;
    private String categoryName;
    private String categoryColor;
    private Long parentId;
    private String title;
    private String description;
    private String priority;
    private Integer status;
    private LocalDateTime deadline;
    private LocalDateTime reminderTime;
    private LocalDateTime completedTime;
    private String tags;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
