package com.aihelp.dto.response;

import lombok.Data;
import lombok.Builder;
import java.time.LocalDateTime;

@Data
@Builder
public class CategoryResponse {
    private Long id;
    private Long userId;
    private String categoryName;
    private String categoryColor;
    private Integer sortOrder;
    private LocalDateTime createTime;
}
