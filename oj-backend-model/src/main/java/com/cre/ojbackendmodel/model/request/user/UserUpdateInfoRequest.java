package com.cre.ojbackendmodel.model.request.user;


import lombok.Data;

import java.io.Serializable;

/**
 * 用户信息更新请求体
 */
@Data
public class UserUpdateInfoRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;


    private String username;


    private String userEmail;

    /**
     * 个性签名
     */
    private String signature;

    /**
     * 手机号
     */
    private String telephone;

    /**
     * 住址
     */
    private String address;

    /**
     * 个人简介
     */
    private String description;


}
