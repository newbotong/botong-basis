package com.yunjing.zuul.permission.filter;

import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import com.yunjing.mommon.wrapper.ResponseEntityWrapper;
import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @version 1.0.0
 * @author: Gyb
 * @date 2018/4/13
 * @description
 **/
//@RestController
//public class ErrorHandlerController implements ErrorController {
//
//    /**
//     * 出异常后进入该方法，交由下面的方法处理
//     */
//    @Override
//    public String getErrorPath() {
//        return "/error";
//    }
//
//    @RequestMapping("/error")
//    public Object error() {
//        RequestContext ctx = RequestContext.getCurrentContext();
//        ZuulException exception = (ZuulException) ctx.getThrowable();
//        return ResponseEntityWrapper.error(exception.nStatusCode, exception.getMessage());
//    }
//
//}
