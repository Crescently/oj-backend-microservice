package com.cre.ojbackendmodel.model.request.user;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户头像更新请求
 */
@Data
public class UserUpdateAvatarRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    private String avatarUrl;

    private Long userId;
}
