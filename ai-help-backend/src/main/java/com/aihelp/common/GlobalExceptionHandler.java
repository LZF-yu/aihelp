package com.aihelp.common;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BadCredentialsException.class)
    public Response<Void> handleBadCredentialsException(BadCredentialsException e) {
        return Response.error(ResultCode.UNAUTHORIZED, "用户名或密码错误");
    }

    @ExceptionHandler(AccessDeniedException.class)
    public Response<Void> handleAccessDeniedException(AccessDeniedException e) {
        return Response.error(ResultCode.FORBIDDEN, "没有访问权限");
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public Response<Void> handleIllegalArgumentException(IllegalArgumentException e) {
        return Response.error(ResultCode.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    public Response<Void> handleRuntimeException(RuntimeException e) {
        return Response.error(ResultCode.INTERNAL_ERROR, "服务器内部错误: " + e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public Response<Void> handleException(Exception e) {
        return Response.error(ResultCode.INTERNAL_ERROR, "系统异常: " + e.getMessage());
    }
}
