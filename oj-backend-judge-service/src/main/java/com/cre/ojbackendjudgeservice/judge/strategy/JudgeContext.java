package com.cre.ojbackendjudgeservice.judge.strategy;


import com.cre.ojbackendmodel.model.codesandbox.JudgeInfo;
import com.cre.ojbackendmodel.model.entity.Question;
import com.cre.ojbackendmodel.model.entity.QuestionSubmit;
import com.cre.ojbackendmodel.model.request.question.JudgeCase;
import lombok.Data;

import java.util.List;

/**
 * 上下文（用于定义在策略中传递的参数）
 */
@Data
public class JudgeContext {
    private JudgeInfo judgeInfo;
    private List<String> inputList;
    private List<String> outputList;
    private List<JudgeCase>judgeCaseList;
    private Question question;
    private QuestionSubmit questionSubmit;
}