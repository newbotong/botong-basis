package com.yunjing.zuul.processor.feign.fallback;

import com.yunjing.mommon.wrapper.ResponseEntityWrapper;
import com.yunjing.zuul.dto.ResourceDto;

import java.util.Collections;
import java.util.List;

/**
 * @version 1.0.0
 * @author: Gyb
 * @date 2018/3/12
 * @description
 **/
public class AdminUserRemoteServiceFallback {

    ResponseEntityWrapper<List<ResourceDto>> accessResourceListByUser(String userId) {
        return ResponseEntityWrapper.success(Collections.<Long>emptyList());
    }
}
