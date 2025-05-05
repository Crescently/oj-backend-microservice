package com.cre.ojbackenduserservice.service;

import com.cre.ojbackendmodel.model.entity.SignInRecord;

import java.util.List;


public interface SignInService {
    boolean signIn(Long userId);

    List<SignInRecord> getSignedDates(Long userId);
}
