package com.yunjing.zuul.processor.feign.fallback;

import com.yunjing.mommon.constant.StatusCode;
import com.yunjing.mommon.wrapper.RpcResponseWrapper;
import org.springframework.stereotype.Component;

/**
 * @version 1.0.0
 * @author: Gyb
 * @date 11/03/2018
 * @description
 **/
@Component
public class TokenRemoteServiceFallback {


    public RpcResponseWrapper authentication(String accessToken) {
        return RpcResponseWrapper.error(StatusCode.FORBIDDEN);
    }


    public RpcResponseWrapper decodeToken(String accessToken) {
        return RpcResponseWrapper.error(StatusCode.FORBIDDEN);
    }
}
