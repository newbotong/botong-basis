package com.yunjing.zuul.processor.feign.fallback;

import com.yunjing.mommon.constant.StatusCode;
import com.yunjing.mommon.wrapper.ResponseEntityWrapper;
import com.yunjing.mommon.wrapper.RpcResponseWrapper;
import com.yunjing.zuul.dto.JwtUserDto;
import org.springframework.stereotype.Component;

/**
 * @version 1.0.0
 * @author: Gyb
 * @date 11/03/2018
 * @description
 **/
public class TokenRemoteServiceFallback {


    public ResponseEntityWrapper<JwtUserDto> authentication(String accessToken) {
        return ResponseEntityWrapper.error(StatusCode.FORBIDDEN);
    }

}
