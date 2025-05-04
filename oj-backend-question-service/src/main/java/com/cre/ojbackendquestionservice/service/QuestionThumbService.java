package com.cre.ojbackendquestionservice.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cre.ojbackendmodel.model.entity.QuestionThumb;
import com.cre.ojbackendmodel.model.entity.User;


public interface QuestionThumbService extends IService<QuestionThumb> {


    int doQuestionThumb(Long questionId, User loginUser);


    int doQuestionThumbInner(Long userId, Long questionId);
}
