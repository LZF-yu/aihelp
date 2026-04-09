package com.aihelp.dto.request;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserUpdateRequest {

    @Size(max = 50, message = "昵称长度不能超过50个字符")
    private String nickname;

    @Size(max = 100, message = "邮箱长度不能超过100个字符")
    private String email;

    private String avatar;
}
