package com.cre.ojbackendmodel.model.request.user;


import lombok.Data;


import java.io.Serializable;

/**
 * 用户注册请求类
 */
@Data
public class UserRegisterRequest implements Serializable {


    private static final long serialVersionUID = 1L;

    /**
     * 用户账号
     */

    private String userAccount;

    /**
     * 用户密码
     */

    private String userPassword;

    /**
     * 校验密码
     */

    private String checkPassword;

    /**
     * 用户名
     */

    private String username;

    /**
     * 邮箱
     */
    private String userEmail;
}
