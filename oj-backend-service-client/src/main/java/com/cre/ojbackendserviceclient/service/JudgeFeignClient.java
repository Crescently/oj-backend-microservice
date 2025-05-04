package com.cre.ojbackendserviceclient.service;


import com.cre.ojbackendmodel.model.entity.QuestionSubmit;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "oj-backend-judge-service", path = "/api/judge/inner")
public interface JudgeFeignClient {
    /**
     * 判题
     */
    @PostMapping("/do")
    QuestionSubmit doJudge(@RequestParam("questionSubmitId") long questionSubmitId);
}