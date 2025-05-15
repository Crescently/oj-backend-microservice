package com.cre.ojbackenduserservice.service;

import com.cre.ojbackendmodel.model.entity.SignInRecord;

import java.util.List;


public interface SignInService {
    /**
     * 签到
     */
    boolean signIn(Long userId);

    /**
     * 获取签到记录
     */
    List<SignInRecord> getSignedDates(Long userId);
}
