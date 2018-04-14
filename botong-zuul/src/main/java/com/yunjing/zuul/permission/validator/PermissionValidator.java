package com.yunjing.zuul.permission.validator;

import com.netflix.zuul.context.RequestContext;
import com.yunjing.mommon.constant.StatusCode;
import com.yunjing.mommon.global.exception.BaseRuntimeException;
import com.yunjing.mommon.wrapper.ResponseEntityWrapper;
import com.yunjing.zuul.permission.context.PermissionContext;
import com.yunjing.zuul.permission.dao.redis.respository.OrgUserInfoRepository;
import com.yunjing.zuul.permission.dto.JwtUserDto;
import com.yunjing.zuul.permission.dto.MemberInfoDto;
import com.yunjing.zuul.permission.dto.ResourceDto;
import com.yunjing.zuul.permission.processor.feign.AdminUserRemoteService;
import com.yunjing.zuul.permission.utils.HeaderHelper;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @version 1.0.0
 * @author: Gyb
 * @date 2018/4/13
 * @description
 **/
@Component
public class PermissionValidator implements Validator {

    private static final Logger logger = LoggerFactory.getLogger(PermissionValidator.class);

    @Value("${zuul.prefix}")
    private String prefix;

    @Resource
    @Lazy
    private AdminUserRemoteService adminUserRemoteService;

    @Resource
    @Lazy
    private OrgUserInfoRepository orgUserInfoRepository;

    /**
     * 获取请求路径
     *
     * @return 去除zuul prifix的请求路径
     */
    private String prepareRequestUri(String prefix) {
        final String requestUri = RequestContext.getCurrentContext().getRequest().getRequestURI().substring(prefix.length());
        logger.info("request url = {}", requestUri);
        return requestUri;
    }

    /**
     * 根绝id获取用户的可访问资源
     *
     * @param id passportId或者memberId
     * @return 可访问资源列表
     */
    private List<ResourceDto> getCurrentUserAccessibleResource(String id) {
        ResponseEntityWrapper<List<ResourceDto>> response = adminUserRemoteService.accessResourceListByUser(id);
        if (response.getStatusCode() == StatusCode.SUCCESS.getStatusCode()) {
            return response.getData();
        } else {
            throw new BaseRuntimeException(response.getStatusCode(), response.getStatusMessage());
        }
    }

    /**
     * 判断memberId对应的passportId和token中的passportId是否相等
     *
     * @param jwtUser
     * @param memberId
     * @return
     */
    private boolean validateMember(JwtUserDto jwtUser, String memberId) {
        MemberInfoDto memberInfo = orgUserInfoRepository.getMemberInfoById(memberId);
        return StringUtils.equals(jwtUser.getIdentity(), memberInfo.getPassportId());
    }

    @Override
    public void validate() {
        final String requestUri = prepareRequestUri(prefix);
        final RequestContext context = RequestContext.getCurrentContext();
        final String method = context.getRequest().getMethod();
        final String memberId = HeaderHelper.getMemberId(context);

        //当memberId参数为空时忽略memberId
        boolean ignoreMemberId = StringUtils.isEmpty(memberId);

        /*
          先验证memberId和passportId
         */
        PermissionContext permissionContext = PermissionContext.getCurrentContext();
        JwtUserDto jwtUserDto = permissionContext.getJwtUser();

        //不忽略memberId时，校验memberId换取得passportId和token中passportId比较,不相等时，返回权限错误异常
        if (!ignoreMemberId && !validateMember(jwtUserDto, memberId)) {
            throw new BaseRuntimeException(StatusCode.FORBIDDEN);
        }
        //获取当前用户passport所对应的可访问资源
        List<ResourceDto> accessibleResourceList = getCurrentUserAccessibleResource(jwtUserDto.getIdentity());

        //如果不忽略,追加memberId可访问资源列表
        if (!ignoreMemberId) {
            accessibleResourceList.addAll(getCurrentUserAccessibleResource(memberId));
        }

        //如果访问资源是空，抛出权限异常
        if (CollectionUtils.isEmpty(accessibleResourceList)) {
            throw new BaseRuntimeException(StatusCode.FORBIDDEN);
        }

        boolean matched = accessibleResourceList.parallelStream().distinct().anyMatch(resourceDto -> resourceDto.getMethod().equalsIgnoreCase(method)
                && resourceDto.getUri().equalsIgnoreCase(requestUri));

        if (!matched) {
            throw new BaseRuntimeException(StatusCode.FORBIDDEN);
        }
    }
}