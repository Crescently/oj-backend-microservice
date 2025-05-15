package com.cre.ojbackendmodel.model.request.admin;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户创建请求 （管理员）
 */
@Data
public class UserAddRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户账号
     */
    private String userAccount;

    /**
     * 用户名
     */
    private String username;

    /**
     * 邮箱
     */
    private String userEmail;

    /**
     * 手机号
     */
    private String telephone;

    /**
     * 住址
     */
    private String address;


    /**
     * 用户角色
     */
    private String userRole;


}
