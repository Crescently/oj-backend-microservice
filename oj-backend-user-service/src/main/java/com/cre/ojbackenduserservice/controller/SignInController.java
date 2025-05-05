package com.cre.ojbackenduserservice.controller;

import com.cre.ojbackendcommon.common.BaseResponse;
import com.cre.ojbackendmodel.model.entity.SignInRecord;
import com.cre.ojbackendmodel.model.entity.User;
import com.cre.ojbackendmodel.model.request.user.IdRequest;
import com.cre.ojbackenduserservice.service.SignInService;
import com.cre.ojbackenduserservice.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/sign-in")
public class SignInController {

    @Resource
    private SignInService signInService;

    @Resource
    private UserService userService;

    @PostMapping("/post")
    public BaseResponse signIn(@RequestBody IdRequest idRequest) {
        boolean success = signInService.signIn(idRequest.getUserId());
        return BaseResponse.success(success);
    }

    @GetMapping("/get")
    public BaseResponse<List<SignInRecord>> getSignedDates(HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request);
        return BaseResponse.success(signInService.getSignedDates(loginUser.getId()));
    }
}
