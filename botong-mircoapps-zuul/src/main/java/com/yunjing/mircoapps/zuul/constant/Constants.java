package com.yunjing.mircoapps.zuul.constant;

/**
 * @version 1.0.0
 * @author: Gyb
 * @date 2018/4/13
 * @description
 **/
public interface Constants {

    interface Header {

        /**
         * token头
         */
        String HEADER_BEARER = "Beare";

        /**
         * 权限头
         */
        String HEADER_AUTHORIZATION = "Authorization";

        /**
         * 企业用户Id头
         */
        String HEADER_MEMBER_ID = "memberId";

        /**
         * gateway转发的passportId头
         */
        String HEADER_USER_ID = "i";

        /**
         * gateway转发的用户信息头
         */
        String HEADER_USER_INFO = "u";

    }

    interface FilterOrder {

        int IGNORE_FILTER_ORDER = 10;

        int TOKEN_FILER_ORDER = 11;

        int PERMISSION_FILTER_ORDER = 12;
    }

    interface Permission {

        String SYSTEM_ADMIN_SERVICE = "botong-admin";
    }
}