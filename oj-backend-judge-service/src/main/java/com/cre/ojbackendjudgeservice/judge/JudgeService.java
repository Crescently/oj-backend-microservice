package com.cre.ojbackendjudgeservice.judge;


import com.cre.ojbackendmodel.model.entity.QuestionSubmit;

public interface JudgeService {

    QuestionSubmit doJudge(long questionSubmitId);
}
