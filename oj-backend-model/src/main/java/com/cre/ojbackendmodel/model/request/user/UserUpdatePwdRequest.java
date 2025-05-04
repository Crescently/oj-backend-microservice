package com.cre.ojbackendmodel.model.request.user;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户修改密码请求体
 */
@Data
public class UserUpdatePwdRequest implements Serializable {


    private static final long serialVersionUID = 1L;

    private Long userId;


    private String oldPassword;


    private String newPassword;


    private String rePassword;


}
