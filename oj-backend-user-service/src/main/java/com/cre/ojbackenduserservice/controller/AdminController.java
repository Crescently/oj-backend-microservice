package com.cre.ojbackenduserservice.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cre.ojbackendcommon.common.BaseResponse;
import com.cre.ojbackendcommon.common.DeleteRequest;
import com.cre.ojbackendcommon.common.ErrorCode;
import com.cre.ojbackendcommon.exception.BusinessException;
import com.cre.ojbackendmodel.model.entity.User;
import com.cre.ojbackendmodel.model.request.admin.UserAddRequest;
import com.cre.ojbackendmodel.model.request.admin.UserInfoUpdateRequest;
import com.cre.ojbackendmodel.model.request.admin.UserQueryRequest;
import com.cre.ojbackenduserservice.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/admin")
@Slf4j
public class AdminController {

    @Resource
    private UserService userService;

    /**
     * 获取用户列表 (仅管理员)
     */
    @PostMapping("/list")
    public BaseResponse<Page<User>> listUserByPage(@RequestBody UserQueryRequest userQueryRequest) {
        long current = userQueryRequest.getCurrent();
        long size = userQueryRequest.getPageSize();
        Page<User> userPage = userService.page(new Page<>(current, size), userService.getQueryWrapper(userQueryRequest));
        return BaseResponse.success(userPage);
    }

    /**
     * 删除用户
     */
    @DeleteMapping("/delete")
    public BaseResponse<?> deleteUser(@RequestBody DeleteRequest deleteRequest) {
        if (deleteRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        userService.deleteUser(deleteRequest);
        return BaseResponse.success();
    }

    /**
     * 添加用户
     */
    @PutMapping("/add")
    public BaseResponse<?> addUser(@RequestBody UserAddRequest userAddRequest) {
        if (userAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        userService.addUser(userAddRequest);
        return BaseResponse.success();
    }

    /**
     * 更新用户
     */
    @PutMapping("/update")
    public BaseResponse updateUser(@RequestBody UserInfoUpdateRequest userInfoUpdateRequest) {
        if (userInfoUpdateRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        userService.updateUser(userInfoUpdateRequest);
        return BaseResponse.success();
    }

}
