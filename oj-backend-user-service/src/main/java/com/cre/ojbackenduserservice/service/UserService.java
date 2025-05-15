package com.cre.ojbackenduserservice.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cre.ojbackendcommon.common.DeleteRequest;
import com.cre.ojbackendmodel.model.entity.User;
import com.cre.ojbackendmodel.model.request.admin.UserAddRequest;
import com.cre.ojbackendmodel.model.request.admin.UserInfoUpdateRequest;
import com.cre.ojbackendmodel.model.request.admin.UserQueryRequest;
import com.cre.ojbackendmodel.model.request.user.UserRegisterRequest;
import com.cre.ojbackendmodel.model.request.user.UserUpdateAvatarRequest;
import com.cre.ojbackendmodel.model.request.user.UserUpdateInfoRequest;
import com.cre.ojbackendmodel.model.request.user.UserUpdatePwdRequest;
import com.cre.ojbackendmodel.model.vo.LoginUserVO;
import com.cre.ojbackendmodel.model.vo.UserVO;

import javax.servlet.http.HttpServletRequest;


public interface UserService extends IService<User> {

    /**
     * 用户注册
     */
    void register(UserRegisterRequest userRegisterRequest);

    /**
     * 更新用户信息
     */
    void updateUserInfo(UserUpdateInfoRequest userUpdateInfoRequest);

    /**
     * 更新用户头像
     */
    void updateAvatar(UserUpdateAvatarRequest userUpdateAvatarRequest);

    /**
     * 更新用户密码
     */
    void updatePassword(UserUpdatePwdRequest userUpdatePwdRequest);

    /**
     * 用户登录
     */
    LoginUserVO login(String userAccount, String userPassword, HttpServletRequest request);

    /**
     * 用户登出
     */
    boolean userLogout(HttpServletRequest request);

    /**
     * 查询用户
     */
    QueryWrapper<User> getQueryWrapper(UserQueryRequest userQueryRequest);

    /**
     * 删除用户
     */
    void deleteUser(DeleteRequest deleteRequest);

    /**
     * 添加用户
     */
    void addUser(UserAddRequest userAddRequest);

    /**
     * 管理员更新用户
     */
    void updateUser(UserInfoUpdateRequest userInfoUpdateRequest);

    /**
     * 获取脱敏的用户信息
     */
    UserVO getUserVO(User user);

    /**
     * 获取当前登录用户
     */
    User getLoginUser(HttpServletRequest request);

    /**
     * 是否为管理员
     */
    boolean isAdmin(User user);
}
