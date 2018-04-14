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

        if (StringUtils.isEmpty(memberId)) {
            throw new BaseRuntimeException(StatusCode.MISSING_REQUIRE_FIELD);
        }
        /**
         * 先验证memberId和passportId
         */
        PermissionContext permissionContext = PermissionContext.getCurrentContext();
        JwtUserDto jwtUserDto = permissionContext.getJwtUser();
        if (!validateMember(jwtUserDto, memberId)) {
            throw new BaseRuntimeException(StatusCode.FORBIDDEN);
        }

        List<ResourceDto> accessibleResourceList = getCurrentUserAccessibleResource(jwtUserDto.getIdentity());
        //passportId对应的可访问资源列表不存在，则用用memberId尝试
        if (CollectionUtils.isEmpty(accessibleResourceList)) {
            accessibleResourceList = getCurrentUserAccessibleResource(memberId);
        } else {
            //passportId对应的访问资源存在，合并memberId和passportId的可访问资源列表
            accessibleResourceList.addAll(getCurrentUserAccessibleResource(memberId));
        }

        boolean matched = accessibleResourceList.parallelStream().distinct().anyMatch(resourceDto -> resourceDto.getMethod().equalsIgnoreCase(method)
                && resourceDto.getUri().equalsIgnoreCase(requestUri));

        if (!matched) {
            throw new BaseRuntimeException(StatusCode.FORBIDDEN);
        }
    }
}