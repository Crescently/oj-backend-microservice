package com.cre.ojbackendjudgeservice.judge;

import cn.hutool.json.JSONUtil;
import com.cre.ojbackendcommon.common.ErrorCode;
import com.cre.ojbackendcommon.exception.BusinessException;
import com.cre.ojbackendjudgeservice.judge.codesandbox.CodeSandbox;
import com.cre.ojbackendjudgeservice.judge.codesandbox.CodeSandboxFactory;
import com.cre.ojbackendjudgeservice.judge.codesandbox.CodeSandboxProxy;
import com.cre.ojbackendjudgeservice.judge.strategy.JudgeContext;
import com.cre.ojbackendmodel.model.codesandbox.ExecuteCodeRequest;
import com.cre.ojbackendmodel.model.codesandbox.ExecuteCodeResponse;
import com.cre.ojbackendmodel.model.codesandbox.JudgeInfo;
import com.cre.ojbackendmodel.model.entity.Question;
import com.cre.ojbackendmodel.model.entity.QuestionSubmit;
import com.cre.ojbackendmodel.model.enums.JudgeInfoMessageEnum;
import com.cre.ojbackendmodel.model.enums.QuestionSubmitStatusEnum;
import com.cre.ojbackendmodel.model.request.question.JudgeCase;
import com.cre.ojbackendserviceclient.service.QuestionFeignClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class JudgeServiceImpl implements JudgeService {
    @Resource
    private QuestionFeignClient questionFeignClient;

    @Resource
    private JudgeManager judgeManager;

    @Value("${codesandbox.type:example}")
    private String type;


    @Override
    public QuestionSubmit doJudge(long questionSubmitId) {
        //传入提交的id，获取对应的题目信息
        QuestionSubmit questionSubmit = questionFeignClient.getQuestionSubmitById(questionSubmitId);
        if (questionSubmit == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "提交信息不存在");
        }
        Long questionId = questionSubmit.getQuestionId();
        Question question = questionFeignClient.getQuestionById(questionId);
        if (question == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "题目不存在");
        }
        //如果不为等待状态，抛出异常
        if (!questionSubmit.getStatus().equals(QuestionSubmitStatusEnum.WAITING.getValue())) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "题目正在判题中");
        }
        //更改题目状态为"判题中"，防止重复执行
        QuestionSubmit questionSubmitUpdate = new QuestionSubmit();
        questionSubmitUpdate.setId(questionSubmitId);
        questionSubmitUpdate.setStatus(QuestionSubmitStatusEnum.RUNNING.getValue());
        boolean update = questionFeignClient.updateQuestionSubmitById(questionSubmitUpdate);
        if (!update) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "题目状态更新错误");
        }
        //调用代码沙箱，获取执行结果
        CodeSandbox codeSandbox = CodeSandboxFactory.newInstance(type);
        codeSandbox = new CodeSandboxProxy(codeSandbox);
        String language = questionSubmit.getLanguage();
        String code = questionSubmit.getCode();
        //获取输入用例
        String judgeCaseStr = question.getJudgeCase();
        List<JudgeCase> judgeCaseList = JSONUtil.toList(judgeCaseStr, JudgeCase.class);
        List<String> inputList = judgeCaseList.stream().map(JudgeCase::getInput).collect(Collectors.toList());
        ExecuteCodeRequest executeCodeRequest = ExecuteCodeRequest.builder().code(code).language(language).inputList(inputList).build();
        // 执行代码
        ExecuteCodeResponse executeCodeResponse = codeSandbox.executeCode(executeCodeRequest);
        //先判断沙箱执行的结果输出数量是否和预期输出数量相等
        List<String> outputList = executeCodeResponse.getOutputList();
        //5.根据沙箱的执行结果，设置题目的判题状态和信息
        JudgeContext judgeContext = new JudgeContext();
        judgeContext.setJudgeInfo(executeCodeResponse.getJudgeInfo());
        judgeContext.setInputList(inputList);
        judgeContext.setOutputList(outputList);
        judgeContext.setJudgeCaseList(judgeCaseList);
        judgeContext.setQuestion(question);
        judgeContext.setQuestionSubmit(questionSubmit);
        JudgeInfo judgeInfo = judgeManager.doJudge(judgeContext);
        // 填充修改数据库的字段
        questionSubmitUpdate = new QuestionSubmit();
        questionSubmitUpdate.setId(questionSubmitId);
        questionSubmitUpdate.setJudgeInfo(JSONUtil.toJsonStr(judgeInfo));
        if (judgeInfo.getMessage().equals(JudgeInfoMessageEnum.ACCEPT.getValue())) {
            questionSubmitUpdate.setStatus(QuestionSubmitStatusEnum.SUCCESS.getValue());
        } else {
            questionSubmitUpdate.setStatus(QuestionSubmitStatusEnum.FAILED.getValue());
        }
        //6.修改数据库中的判题结果
        update = questionFeignClient.updateQuestionSubmitById(questionSubmitUpdate);
        if (!update) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "题目状态更新错误");
        }
        return questionFeignClient.getQuestionSubmitById(questionId);
    }
}
