package com.cre.ojbackenduserservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cre.ojbackendmodel.model.entity.SignInRecord;
import com.cre.ojbackenduserservice.mapper.SignInMapper;
import com.cre.ojbackenduserservice.service.SignInService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class SignInServiceImpl extends ServiceImpl<SignInMapper, SignInRecord> implements SignInService {

    @Resource
    private SignInMapper signInMapper;

    @Override
    public boolean signIn(Long userId) {
        LocalDateTime today = LocalDateTime.now();

        QueryWrapper<SignInRecord> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId).eq("sign_date", today);

        if (signInMapper.selectCount(wrapper) > 0) {
            return false;
        }

        SignInRecord record = new SignInRecord();
        record.setUserId(userId);
        record.setSignDate(today);
        signInMapper.insert(record);
        return true;
    }

    @Override
    public List<SignInRecord> getSignedDates(Long userId) {
        return signInMapper.selectList(new QueryWrapper<SignInRecord>().eq("user_id", userId));
    }
}