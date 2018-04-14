package com.yunjing.zuul.permission.context;

import com.netflix.zuul.context.RequestContext;
import com.yunjing.zuul.permission.dto.JwtUserDto;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @version 1.0.0
 * @author: Gyb
 * @date 2018/4/13
 * @description 权限校验上下文
 **/
public class PermissionContext extends ConcurrentHashMap<String, Object> {

    protected static Class<? extends PermissionContext> contextClass = PermissionContext.class;

    protected static final ThreadLocal<? extends PermissionContext> threadLocal = ThreadLocal.withInitial(() -> {
        try {
            return contextClass.newInstance();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    });

    public static PermissionContext getCurrentContext() {
        return threadLocal.get();
    }

    public boolean ignoredPermissionValidate() {
        return (boolean) get("ignoredPermissionValidate");
    }

    public void setIgnoredPermissionValidate(boolean isIgnored) {
        set("ignoredPermissionValidate", isIgnored);
    }

    public void setCurrentUser(JwtUserDto jwtUserDto) {
        set("JwtUser", jwtUserDto);
    }

    public JwtUserDto getJwtUser() {
        return (JwtUserDto) get("JwtUser");
    }

    /**
     * 如果value为null时，则remove
     *
     * @param key
     * @param value
     */
    public void set(String key, Object value) {
        if (null != value) {
            put(key, value);
        } else {
            remove(key);
        }
    }

    public void unset() {
        threadLocal.remove();
    }
}
