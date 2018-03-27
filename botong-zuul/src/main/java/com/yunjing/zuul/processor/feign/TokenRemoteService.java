package com.yunjing.zuul.processor.feign;

import com.yunjing.mommon.wrapper.ResponseEntityWrapper;
import com.yunjing.mommon.wrapper.RpcResponseWrapper;
import com.yunjing.zuul.dto.JwtUserDto;
import com.yunjing.zuul.processor.feign.fallback.TokenRemoteServiceFallback;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @version 1.0.0
 * @author: Gyb
 * @date 11/03/2018
 * @description
 **/
@FeignClient(name = "botong-auth-server",
        fallback = TokenRemoteServiceFallback.class)
public interface TokenRemoteService {

    @PostMapping("/rpc/auth/authentication")
    ResponseEntityWrapper<JwtUserDto> authentication(@RequestBody String accessToken);
}
