package com.aihelp.dto.request;

import lombok.Data;

@Data
public class TaskQueryRequest {
    private Long categoryId;
    private Integer status;
    private String priority;
    private String keyword;
    private Long parentId;
    private Integer current = 1;
    private Integer size = 10;
}
