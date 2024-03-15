package com.ws.inspectionoffice;

import com.ws.inspectionoffice.utils.JsonResponse;
import com.ws.inspectionoffice.utils.ResponseCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    /**
     * 处理其他异常
     *
     */
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public JsonResponse exceptionHandler(Exception e) {
        LOGGER.error(e.getMessage(), e);
        return JsonResponse.wrapper().code(ResponseCode.ERROR_SERVER_ERROR).message(e.getMessage());
    }
}