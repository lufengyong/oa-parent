package com.openoa.admin.handler;

import com.openoa.admin.ultil.R;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public R ExceptionHandler(Exception exp) {
        if (exp instanceof ExpiredJwtException) {
            return R.error("用户登录过期");
        } else {
            return R.error(exp.getMessage());
        }
    }
}
