package com.sowhile.registration.common.exception;

import com.sowhile.registration.common.result.Result;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 全局异常处理类
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result error(Exception e) {
        e.printStackTrace();
        return Result.fail();
    }

    /**
     * 自定义异常处理方法
     */
    @ExceptionHandler(RegistrationException.class)
    @ResponseBody
    public Result error(RegistrationException e) {
        return Result.build(e.getCode(), e.getMessage());
    }
}
