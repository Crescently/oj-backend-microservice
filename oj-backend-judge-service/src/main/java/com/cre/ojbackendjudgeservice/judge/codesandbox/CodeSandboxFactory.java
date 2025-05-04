package com.cre.ojbackendjudgeservice.judge.codesandbox;


import com.cre.ojbackendjudgeservice.judge.codesandbox.impl.ExampleCodeSandbox;
import com.cre.ojbackendjudgeservice.judge.codesandbox.impl.RemoteCodeSandbox;
import com.cre.ojbackendjudgeservice.judge.codesandbox.impl.ThirdPartyCodeSandbox;

/**
 * 代码沙箱工厂（根据字符串参数创建对应的沙箱实例）
 */
public class CodeSandboxFactory {

    /**
     * 创建沙箱实例
     *
     * @param type 沙箱类型
     * @return 沙箱实例
     */
    public static CodeSandbox newInstance(String type) {
        switch (type) {
            case "example":
                return new ExampleCodeSandbox();
            case "remote":
                return new RemoteCodeSandbox();
            case "thirdParty":
                return new ThirdPartyCodeSandbox();
            default:
                return new ExampleCodeSandbox();
        }
    }

}
