package com.yunjing.zuul.permission.dao.redis.respository;

import com.alibaba.fastjson.JSON;
import com.common.redis.BaseRedisService;
import com.yunjing.zuul.permission.dao.redis.template.ReadonlyStringRedisTemplate;
import com.yunjing.zuul.permission.dto.MemberInfoDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.stereotype.Component;

/**
 * @version 1.0.0
 * @author: Gyb
 * @date 2018/4/13
 * @description
 **/
@Component
public class OrgUserInfoRepository {

    @Autowired
    private ReadonlyStringRedisTemplate readonlyStringRedisTemplate;

    protected String getRedisKey() {
        return "botong:org:member";
    }

    public MemberInfoDto getMemberInfoById(String memberId) {
        HashOperations<String, String, String> hashOperations = readonlyStringRedisTemplate.opsForHash();
        String json = hashOperations.get(getRedisKey(), memberId);
        return JSON.parseObject(json, MemberInfoDto.class);
    }
}
