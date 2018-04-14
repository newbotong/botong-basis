package com.yunjing.zuul.permission.processor.feign.fallback;

import com.yunjing.mommon.constant.StatusCode;
import com.yunjing.mommon.wrapper.ResponseEntityWrapper;
import com.yunjing.zuul.permission.dto.JwtUserDto;
import com.yunjing.zuul.permission.processor.feign.TokenRemoteService;
import lombok.Builder;

/**
 * @version 1.0.0
 * @author: Gyb
 * @date 11/03/2018
 * @description
 **/
public class TokenRemoteServiceFallback implements TokenRemoteService {

    @Override
    public ResponseEntityWrapper<JwtUserDto> authentication(String accessToken) {
        return ResponseEntityWrapper.error(StatusCode.HTTP_RESPONSE_ERROR);
    }

}
