package com.aihelp.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AIResponse {
    private String content;
    private String type;
    private long usage;
}
