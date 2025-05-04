package com.cre.ojbackendjudgeservice.judge.codesandbox.impl;


import com.cre.ojbackendjudgeservice.judge.codesandbox.CodeSandbox;
import com.cre.ojbackendmodel.model.codesandbox.ExecuteCodeRequest;
import com.cre.ojbackendmodel.model.codesandbox.ExecuteCodeResponse;
import com.cre.ojbackendmodel.model.codesandbox.JudgeInfo;
import com.cre.ojbackendmodel.model.enums.JudgeInfoMessageEnum;
import com.cre.ojbackendmodel.model.enums.QuestionSubmitStatusEnum;

/**
 * 示例代码沙箱
 */
public class ExampleCodeSandbox implements CodeSandbox {
    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        ExecuteCodeResponse executeCodeResponse = new ExecuteCodeResponse();
        executeCodeResponse.setOutputList(executeCodeRequest.getInputList());
        executeCodeResponse.setMessage("Success");
        executeCodeResponse.setStatus(QuestionSubmitStatusEnum.SUCCESS.getValue());
        JudgeInfo judgeInfo = new JudgeInfo();
        judgeInfo.setMessage(JudgeInfoMessageEnum.ACCEPT.getValue());
        judgeInfo.setMemory(100L);
        judgeInfo.setTime(400L);
        executeCodeResponse.setJudgeInfo(judgeInfo);
        return executeCodeResponse;
    }
}
