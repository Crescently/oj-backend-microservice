package com.cre.ojbackendjudgeservice.judge.codesandbox;

import com.cre.ojbackendmodel.model.codesandbox.ExecuteCodeRequest;
import com.cre.ojbackendmodel.model.codesandbox.ExecuteCodeResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CodeSandboxProxy implements CodeSandbox {

    private final CodeSandbox codeSandbox;

    public CodeSandboxProxy(CodeSandbox codeSandbox) {
        this.codeSandbox = codeSandbox;
    }

    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        ExecuteCodeResponse executeCodeResponse = codeSandbox.executeCode(executeCodeRequest);
        log.info("相应数据{}", executeCodeResponse.toString());
        return executeCodeResponse;
    }
}
