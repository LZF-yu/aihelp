package com.aihelp.dto.request;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CategoryRequest {

    @Size(max = 50, message = "分类名称长度不能超过50个字符")
    private String categoryName;

    @Size(max = 20, message = "分类颜色长度不能超过20个字符")
    private String categoryColor;

    private Integer sortOrder;
}
