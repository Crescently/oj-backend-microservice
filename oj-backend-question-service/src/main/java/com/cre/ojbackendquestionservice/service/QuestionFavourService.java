package com.cre.ojbackendquestionservice.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cre.ojbackendmodel.model.entity.Question;
import com.cre.ojbackendmodel.model.entity.QuestionFavour;
import com.cre.ojbackendmodel.model.entity.User;


public interface QuestionFavourService extends IService<QuestionFavour> {


    int doQuestionFavour(Long questionId, User loginUser);


    Page<Question> listFavourQuestionByPage(IPage<Question> page, Wrapper<Question> queryWrapper,
                                            Long userId);


    int doQuestionFavourInner(Long userId, Long questionId);

}
