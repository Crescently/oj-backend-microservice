package com.cre.ojbackendjudgeservice.judge.codesandbox;


import com.cre.ojbackendmodel.model.codesandbox.ExecuteCodeRequest;
import com.cre.ojbackendmodel.model.codesandbox.ExecuteCodeResponse;

public interface CodeSandbox {

    ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest);
}
