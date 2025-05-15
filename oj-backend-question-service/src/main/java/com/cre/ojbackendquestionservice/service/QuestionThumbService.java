package com.cre.ojbackendquestionservice.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cre.ojbackendmodel.model.entity.QuestionThumb;
import com.cre.ojbackendmodel.model.entity.User;


public interface QuestionThumbService extends IService<QuestionThumb> {

    /**
     * 点赞 / 取消点赞
     */
    int doQuestionThumb(Long questionId, User loginUser);

    /**
     * 点赞 / 取消点赞
     */
    int doQuestionThumbInner(Long userId, Long questionId);
}
