package com.cre.ojbackendquestionservice.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cre.ojbackendcommon.common.BaseResponse;
import com.cre.ojbackendcommon.common.ErrorCode;
import com.cre.ojbackendcommon.exception.BusinessException;
import com.cre.ojbackendmodel.model.entity.Question;
import com.cre.ojbackendmodel.model.entity.User;
import com.cre.ojbackendmodel.model.request.question.QuestionQueryRequest;
import com.cre.ojbackendmodel.model.request.questionfavour.QuestionFavourAddRequest;
import com.cre.ojbackendmodel.model.vo.QuestionVO;
import com.cre.ojbackendquestionservice.service.QuestionFavourService;
import com.cre.ojbackendquestionservice.service.QuestionService;
import com.cre.ojbackendserviceclient.service.UserFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/favour")
@Slf4j
public class QuestionFavourController {

    @Resource
    private QuestionFavourService questionFavourService;

    @Resource
    private QuestionService questionService;

    @Resource
    private UserFeignClient userFeignClient;

    /**
     * 问题收藏
     *
     * @param questionFavourAddRequest questionFavourAddRequest
     * @param request                  request
     * @return
     */
    @PostMapping("/")
    public BaseResponse<Integer> doQuestionFavour(@RequestBody QuestionFavourAddRequest questionFavourAddRequest, HttpServletRequest request) {
        if (questionFavourAddRequest == null || questionFavourAddRequest.getQuestionId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 登录才能操作
        final User loginUser = userFeignClient.getLoginUser(request);
        if (loginUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        Long questionId = questionFavourAddRequest.getQuestionId();
        int result = questionFavourService.doQuestionFavour(questionId, loginUser);
        return BaseResponse.success(result);
    }

    /**
     * 获取用户自己的收藏的问题列表
     *
     * @param questionQueryRequest questionQueryRequest
     * @param request              request
     * @return
     */
    @PostMapping("/my/list/page")
    public BaseResponse<Page<QuestionVO>> listMyFavourQuestionByPage(@RequestBody QuestionQueryRequest questionQueryRequest, HttpServletRequest request) {
        if (questionQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userFeignClient.getLoginUser(request);
        long current = questionQueryRequest.getCurrent();
        long size = questionQueryRequest.getPageSize();
        if (size > 20) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        Page<Question> questionPage = questionFavourService.listFavourQuestionByPage(new Page<>(current, size), questionService.getQueryWrapper(questionQueryRequest), loginUser.getId());
        return BaseResponse.success(questionService.getQuestionVOPage(questionPage, request));
    }

}
