package com.aihelp.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("task")
public class Task {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long userId;

    private Long categoryId;

    private Long parentId;

    private String title;

    private String description;

    private String priority;

    private Integer status;

    private LocalDateTime deadline;

    private LocalDateTime reminderTime;

    private LocalDateTime completedTime;

    private String tags;

    @TableLogic
    private Integer deleted;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}