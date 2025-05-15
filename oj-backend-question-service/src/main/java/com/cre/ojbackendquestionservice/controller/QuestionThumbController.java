package com.cre.ojbackendquestionservice.controller;


import com.cre.ojbackendcommon.common.BaseResponse;
import com.cre.ojbackendcommon.common.ErrorCode;
import com.cre.ojbackendcommon.exception.BusinessException;
import com.cre.ojbackendmodel.model.entity.User;
import com.cre.ojbackendmodel.model.request.questionthumb.QuestionThumbAddRequest;
import com.cre.ojbackendquestionservice.service.QuestionThumbService;
import com.cre.ojbackendserviceclient.service.UserFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;


/**
 * 问题点赞接口
 */
@RestController
@RequestMapping("/thumb")
@Slf4j
public class QuestionThumbController {

    @Resource
    private QuestionThumbService questionThumbService;

    @Resource
    private UserFeignClient userFeignClient;

    /**
     * 点赞 / 取消点赞
     */
    @PostMapping("/")
    public BaseResponse<Integer> doThumb(@RequestBody QuestionThumbAddRequest questionThumbAddRequest, HttpServletRequest request) {
        if (questionThumbAddRequest == null || questionThumbAddRequest.getQuestionId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 登录才能点赞
        final User loginUser = userFeignClient.getLoginUser(request);
        if (loginUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        Long questionId = questionThumbAddRequest.getQuestionId();
        int result = questionThumbService.doQuestionThumb(questionId, loginUser);
        return BaseResponse.success(result);
    }

}
