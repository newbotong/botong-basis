package com.yunjing.zuul.processor.feign;

import com.yunjing.mommon.wrapper.RpcResponseWrapper;
import com.yunjing.zuul.processor.feign.fallback.AdminUserRemoteServiceFallback;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @version 1.0.0
 * @author: Gyb
 * @date 2018/3/12
 * @description
 **/
@FeignClient(name = "botong-admin", fallback = AdminUserRemoteServiceFallback.class)
public interface AdminUserRemoteService {

    @GetMapping("/rpc/adminUser/accessResourceList/{adminUserId}")
    RpcResponseWrapper accessResourceListByUser(@PathVariable Long adminUserId);
}
