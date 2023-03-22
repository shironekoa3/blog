package me.javac.blog.handle;

import lombok.extern.slf4j.Slf4j;
import me.javac.blog.exception.ServiceException;
import me.javac.blog.utils.AjaxResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public AjaxResult handleException(Exception e) {
        // 将异常信息打印到日志中
        log.error("Exception caught: {}", e.getMessage());
        // 返回自定义的错误信息
        return AjaxResult.error(e.getMessage());
    }

    @ExceptionHandler(ServiceException.class)
    public AjaxResult handleServiceException(ServiceException e) {
        return new AjaxResult(e.getCode(), e.getMessage());
    }

}