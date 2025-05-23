package com.cre.ojbackendquestionservice.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cre.ojbackendmodel.model.entity.Question;
import com.cre.ojbackendmodel.model.entity.QuestionSubmit;
import com.cre.ojbackendmodel.model.request.question.QuestionQueryRequest;
import com.cre.ojbackendmodel.model.vo.QuestionVO;

import javax.servlet.http.HttpServletRequest;


public interface QuestionService extends IService<Question> {
    /**
     * 校验题目合法
     */
    void validQuestion(Question question, boolean b);

    /**
     * 获取问题对象（脱敏）
     */
    QuestionVO getQuestionVO(Question question, HttpServletRequest request);

    /**
     * 获取查询包装类
     */
    Wrapper<Question> getQueryWrapper(QuestionQueryRequest questionQueryRequest);

    /**
     * 获取问题对象分页（脱敏）
     */
    Page<QuestionVO> getQuestionVOPage(Page<Question> questionPage, HttpServletRequest request);

    /**
     * 获取历史提交问题分页（脱敏）
     */
    Page<QuestionVO> getHistoryQuestionVOPage(Page<QuestionSubmit> questionSubmitPage, long userId);

    /**
     * 更新提交和通过数
     */
    void updateSubmitAndAcceptedNum();

    /**
     * 获取问题答案
     */
    String getQuestionAnswerById(Long questionId, HttpServletRequest request);
}
