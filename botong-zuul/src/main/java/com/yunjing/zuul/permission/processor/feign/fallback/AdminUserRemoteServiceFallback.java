package com.yunjing.zuul.permission.processor.feign.fallback;

import com.yunjing.mommon.wrapper.ResponseEntityWrapper;
import com.yunjing.zuul.permission.dto.ResourceDto;
import com.yunjing.zuul.permission.processor.feign.AdminUserRemoteService;

import java.util.Collections;
import java.util.List;

/**
 * @version 1.0.0
 * @author: Gyb
 * @date 2018/3/12
 * @description
 **/
public class AdminUserRemoteServiceFallback implements AdminUserRemoteService {

    @Override
    public ResponseEntityWrapper<List<ResourceDto>> accessResourceListByUser(String userId) {
        return ResponseEntityWrapper.success(Collections.<Long>emptyList());
    }
}
