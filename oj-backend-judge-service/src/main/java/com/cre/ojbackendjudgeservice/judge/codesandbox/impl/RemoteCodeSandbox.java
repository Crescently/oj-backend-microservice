package com.cre.ojbackendjudgeservice.judge.codesandbox.impl;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.cre.ojbackendcommon.common.ErrorCode;
import com.cre.ojbackendcommon.exception.BusinessException;
import com.cre.ojbackendjudgeservice.judge.codesandbox.CodeSandbox;
import com.cre.ojbackendmodel.model.codesandbox.ExecuteCodeRequest;
import com.cre.ojbackendmodel.model.codesandbox.ExecuteCodeResponse;
import org.apache.commons.lang3.StringUtils;

/**
 * 远程代码沙箱（实际调用接口的沙箱）
 */
public class RemoteCodeSandbox implements CodeSandbox {


    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        String url = "http://192.168.50.129:8082/executeCode";
        String json = JSONUtil.toJsonStr(executeCodeRequest);
        String responseStr = HttpUtil.createPost(url)
                .body(json)
                .execute()
                .body();
        if (StringUtils.isBlank(responseStr)) {
            throw new BusinessException(ErrorCode.API_REQUEST_ERROR, "executeCode remoteSandbox error,message=" + responseStr);
        }
        return JSONUtil.toBean(responseStr, ExecuteCodeResponse.class);
    }
}
