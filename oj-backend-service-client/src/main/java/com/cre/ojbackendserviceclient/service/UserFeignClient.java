package com.cre.ojbackendserviceclient.service;

import com.cre.ojbackendcommon.common.ErrorCode;
import com.cre.ojbackendcommon.exception.BusinessException;
import com.cre.ojbackendmodel.model.entity.User;
import com.cre.ojbackendmodel.model.enums.UserRoleEnum;
import com.cre.ojbackendmodel.model.vo.UserVO;
import org.springframework.beans.BeanUtils;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.List;

import static com.cre.ojbackendcommon.constant.UserConstant.USER_LOGIN_STATE;

@FeignClient(name = "oj-backend-user-service", path = "/api/user/inner")
public interface UserFeignClient {

    /**
     * 获取脱敏的用户信息
     */
    default UserVO getUserVO(User user) {
        if (user == null) {
            return null;
        }
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        return userVO;
    }

    /**
     * 获取当前登录用户
     */
    default User getLoginUser(HttpServletRequest request) {
        // 先判断是否已登录
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User currentUser = (User) userObj;
        if (currentUser == null || currentUser.getId() == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        return currentUser;
    }

    ;

    /**
     * 是否为管理员
     */
    default boolean isAdmin(User user) {
        return user != null && UserRoleEnum.ADMIN.getValue().equals(user.getUserRole());
    }

    /**
     * 根据id获取用户
     */
    @GetMapping("/get/id")
    User getById(@RequestParam("userId") long userId);

    /**
     * 根据id获取用户列表
     */
    @GetMapping("/get/ids")
    List<User> listByIds(@RequestParam("idList") Collection<Long> idList);
}
