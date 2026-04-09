package com.aihelp.common;

import lombok.Data;

@Data
public class Response<T> {
    private int code;
    private String message;
    private T data;

    public static <T> Response<T> success() {
        return success(null);
    }

    public static <T> Response<T> success(T data) {
        Response<T> response = new Response<>();
        response.setCode(ResultCode.SUCCESS);
        response.setMessage("操作成功");
        response.setData(data);
        return response;
    }

    public static <T> Response<T> success(String message, T data) {
        Response<T> response = new Response<>();
        response.setCode(ResultCode.SUCCESS);
        response.setMessage(message);
        response.setData(data);
        return response;
    }

    public static <T> Response<T> created() {
        return created(null);
    }

    public static <T> Response<T> created(T data) {
        Response<T> response = new Response<>();
        response.setCode(ResultCode.CREATED);
        response.setMessage("创建成功");
        response.setData(data);
        return response;
    }

    public static <T> Response<T> error(int code, String message) {
        Response<T> response = new Response<>();
        response.setCode(code);
        response.setMessage(message);
        return response;
    }

    public static <T> Response<T> error(String message) {
        return error(ResultCode.INTERNAL_ERROR, message);
    }
}
