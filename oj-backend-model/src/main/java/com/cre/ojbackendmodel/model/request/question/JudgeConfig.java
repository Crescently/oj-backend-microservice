package com.cre.ojbackendmodel.model.request.question;

import lombok.Data;

/**
 * 判题配置
 */
@Data
public class JudgeConfig {
    /**
     * 时间限制
     */
    private Long timeLimit;
    /**
     * 内存限制
     */
    private Long memoryLimit;
    /**
     * 堆栈限制
     */
    private Long stackLimit;
}

