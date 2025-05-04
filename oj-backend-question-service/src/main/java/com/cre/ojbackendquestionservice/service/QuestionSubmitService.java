package com.cre.ojbackendquestionservice.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cre.ojbackendmodel.model.entity.QuestionSubmit;
import com.cre.ojbackendmodel.model.entity.User;
import com.cre.ojbackendmodel.model.request.questionsubmit.QuestionSubmitAddRequest;
import com.cre.ojbackendmodel.model.request.questionsubmit.QuestionSubmitQueryRequest;
import com.cre.ojbackendmodel.model.vo.QuestionSubmitVO;


public interface QuestionSubmitService extends IService<QuestionSubmit> {

    /**
     * 提交问题
     */
    long doQuestionSubmit(QuestionSubmitAddRequest questionSubmitAddRequest, User loginUser);

    /**
     * 获取问题提交对象（脱敏）
     */
    QuestionSubmitVO getQuestionSubmitVO(QuestionSubmit questionSubmit, User loginUser);

    /**
     * 获取查询包装类
     */
    Wrapper<QuestionSubmit> getQueryWrapper(QuestionSubmitQueryRequest questionSubmitQueryRequest);

    /**
     * 获取问题提交对象分页（脱敏）
     */
    Page<QuestionSubmitVO> getQuestionSubmitVOPage(Page<QuestionSubmit> questionSubmitPage, User loginUser);
}
