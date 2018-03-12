package com.yunjing.zuul.processor.feign;

import com.yunjing.mommon.wrapper.RpcResponseWrapper;
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
    RpcResponseWrapper authentication(@RequestBody String accessToken);

    @PostMapping("/prc/auth/decodeToken")
    RpcResponseWrapper decodeToken(@RequestBody String accessToken);
}
