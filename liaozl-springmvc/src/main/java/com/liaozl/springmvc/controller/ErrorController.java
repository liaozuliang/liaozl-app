package com.liaozl.springmvc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/error")
public class ErrorController {

    @ExceptionHandler
    public String error(HttpServletRequest request, Exception ex) {
        request.setAttribute("ex", ex);
        return "/error/error500";
    }

    @RequestMapping("test")
    public String testError() {
        throw new NullPointerException("出错啦！！！");
    }

    @RequestMapping("404")
    public String error404() {
        return "/error/error404";
    }

    @RequestMapping("500")
    public String error500() {
        return "/error/error500";
    }
}
