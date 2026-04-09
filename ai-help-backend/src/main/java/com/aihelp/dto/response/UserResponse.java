package com.aihelp.dto.response;

import lombok.Data;
import lombok.Builder;
import java.time.LocalDateTime;

@Data
@Builder
public class UserResponse {
    private Long id;
    private String username;
    private String email;
    private String nickname;
    private String avatar;
    private LocalDateTime createTime;
}
