package com.ws.inspectionoffice.utils;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.HashMap;
import java.util.Map;

public class ResponseCode {

    public static final int OK = 200;                          //所有成功都返回此代码
    public static final int ERROR_NOT_LOGIN = 401;             //没有登录，不可访问
    public static final int ERROR_LOGIN_ERROR = 402;                          //所有成功都返回此代码
    public static final int ERROR_SERVER_ERROR = 500;          //异常和所有失败都返回此代码
    public static final Map<Class<?>, Integer> ERROR_CODE_MAPPING;

    static {
        ERROR_CODE_MAPPING = new HashMap<>();
        ERROR_CODE_MAPPING.put(SQLIntegrityConstraintViolationException.class, 5010);
    }

}