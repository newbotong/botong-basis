package com.yunjing.mommon.global.exception.handler;

import com.yunjing.mommon.global.exception.BaseRuntimeException;
import com.yunjing.mommon.wrapper.ResponseEntityWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 定义捕获全局系统异常接口，需要java 8 及以上
 */
public interface GlobalExceptionHandler {

    Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(BaseRuntimeException.class)
    @ResponseBody
    default ResponseEntityWrapper exceptionHandler(BaseRuntimeException e) {
        LOGGER.error("系统发生异常，异常原因为：" + e.getMessage(), e);
        return ResponseEntityWrapper.error(e.getCode(), e.getMessage());
    }
}
