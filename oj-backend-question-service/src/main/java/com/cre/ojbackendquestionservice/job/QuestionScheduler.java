package com.cre.ojbackendquestionservice.job;

import com.cre.ojbackendquestionservice.service.QuestionService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 定时任务
 */
@Component
public class QuestionScheduler {

    @Resource
    private QuestionService questionService;

    // 每5min执行一次
    @Scheduled(cron = "0 */5 * * * ?")
    public void updateQuestionStats() {
        questionService.updateSubmitAndAcceptedNum();
    }
}
