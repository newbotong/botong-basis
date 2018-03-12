package com.yunjing.zuul.processor.feign.fallback;

import com.yunjing.mommon.wrapper.RpcResponseWrapper;
import com.yunjing.zuul.dto.ResourceDto;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;

/**
 * @version 1.0.0
 * @author: Gyb
 * @date 2018/3/12
 * @description
 **/
@Component
public class AdminUserRemoteServiceFallback {

    public RpcResponseWrapper accessResourceListByUser(@PathVariable Long adminUserId) {
        return RpcResponseWrapper.success(new ArrayList<ResourceDto>());
    }
}
