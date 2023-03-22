package me.javac.blog.controller;

import me.javac.blog.exception.ServiceException;
import me.javac.blog.utils.AjaxResult;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class CustomErrorController implements ErrorController {
    private final static String ERROR_PATH = "/error";

    @RequestMapping(ERROR_PATH)
    public AjaxResult handleError(HttpServletRequest request) {
        Exception exception = (Exception) request.getAttribute("javax.servlet.error.exception");
        if (exception != null) {
            if (exception instanceof ServiceException) {
                return new AjaxResult(((ServiceException) exception).getCode(), exception.getMessage());
            }
            return AjaxResult.error(exception.getMessage());
        } else {
            return AjaxResult.error();
        }
    }

}
