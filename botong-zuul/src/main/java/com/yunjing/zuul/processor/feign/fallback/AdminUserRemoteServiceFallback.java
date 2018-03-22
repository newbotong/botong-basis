package com.yunjing.zuul.processor.feign.fallback;

import com.yunjing.mommon.wrapper.RpcResponseWrapper;
import com.yunjing.zuul.dto.ResourceDto;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @version 1.0.0
 * @author: Gyb
 * @date 2018/3/12
 * @description
 **/
public class AdminUserRemoteServiceFallback {

    RpcResponseWrapper accessResourceListByUser(Long userId) {
        return RpcResponseWrapper.success(Collections.<Long>emptyList());
    }
}
