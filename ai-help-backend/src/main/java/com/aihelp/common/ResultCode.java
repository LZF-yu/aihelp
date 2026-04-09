package com.aihelp.common;

public class ResultCode {
    public static final int SUCCESS = 200;
    public static final int CREATED = 201;
    public static final int BAD_REQUEST = 400;
    public static final int UNAUTHORIZED = 401;
    public static final int FORBIDDEN = 403;
    public static final int NOT_FOUND = 404;
    public static final int INTERNAL_ERROR = 500;
    public static final int SERVICE_UNAVAILABLE = 503;

    public static final int TOKEN_EXPIRED = 4011;
    public static final int TOKEN_INVALID = 4012;
    public static final int USERNAME_EXIST = 4001;
    public static final int EMAIL_EXIST = 4002;
}
