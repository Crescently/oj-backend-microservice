package com.cre.ojbackenduserservice.controller.inner;


import com.cre.ojbackendmodel.model.entity.User;
import com.cre.ojbackendserviceclient.service.UserFeignClient;
import com.cre.ojbackenduserservice.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;

/**
 * 该服务仅内部调用
 */
@RestController
@RequestMapping("/inner")
public class UserInnerController implements UserFeignClient {
    @Resource
    private UserService userService;

    /**
     * 根据id获取用户
     */
    @Override
    @GetMapping("/get/id")
    public User getById(@RequestParam("userId") long userId) {
        return userService.getById(userId);
    }

    /**
     * 根据id获取用户列表
     */
    @Override
    @GetMapping("/get/ids")
    public List<User> listByIds(@RequestParam("idList") Collection<Long> idList) {
        return userService.listByIds(idList);
    }
}