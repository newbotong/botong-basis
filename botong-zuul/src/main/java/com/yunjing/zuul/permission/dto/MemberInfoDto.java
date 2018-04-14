package com.yunjing.zuul.permission.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Set;

/**
 * @version 1.0.0
 * @author: Gyb
 * @date 2018/4/13
 * @description redis中的member信息
 **/
@Data
public class MemberInfoDto implements Serializable {

    /**
     * memberId
     */
    private String id;

    /**
     * 成员对应账户Id
     */
    private String passportId;

    /**
     * 成员所在企业Id
     */
    private String companyId;
    /**
     * 职位
     */
    private String position;
    /**
     * 电话号
     */
    private String mobile;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 成员所在部门Id集合
     */
    private Set<String> deptIds;
    /**
     * 总公司id
     */
    private String companyHeadquartersId;

}
