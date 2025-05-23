package com.cre.ojbackendjudgeservice.judge.strategy;


import com.cre.ojbackendmodel.model.codesandbox.JudgeInfo;

/**
 * 判题策略
 */
public interface JudgeStrategy {
    /**
     * 执行判题
     */
    JudgeInfo doJudge(JudgeContext judgeContext);
}