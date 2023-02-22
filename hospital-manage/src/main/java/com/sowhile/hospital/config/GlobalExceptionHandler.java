package com.sowhile.hospital.config;

import com.sowhile.hospital.util.RegistrationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public String error(Exception e) {
        e.printStackTrace();
        return "error";
    }

    /**
     * 自定义异常处理方法
     *
     * @param e
     * @return
     */
    @ExceptionHandler(RegistrationException.class)
    public String error(RegistrationException e, Model model) {
        model.addAttribute("message", e.getMessage());
        return "error";
    }
}