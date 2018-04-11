package com.yunjing.zuul.processor.feign;

import com.yunjing.mommon.wrapper.ResponseEntityWrapper;
import com.yunjing.zuul.dto.ResourceDto;
import com.yunjing.zuul.processor.feign.fallback.AdminUserRemoteServiceFallback;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @version 1.0.0
 * @author: Gyb
 * @date 2018/3/12
 * @description
 **/
@FeignClient(name = "botong-admin", fallback = AdminUserRemoteServiceFallback.class)
public interface AdminUserRemoteService {

    /**
     * 获取当前用户可访问的资源列表rpc调用
     *
     * @param userId
     * @return
     */
    @GetMapping("/rpc/adminUser/get-accessible-resource-list")
    ResponseEntityWrapper<List<ResourceDto>> accessResourceListByUser(@RequestParam("userId") String userId);
}
