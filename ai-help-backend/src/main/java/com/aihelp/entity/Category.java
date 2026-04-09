package com.aihelp.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("task_category")
public class Category {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long userId;

    private String categoryName;

    private String categoryColor;

    private Integer sortOrder;

    @TableLogic
    private Integer deleted;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}