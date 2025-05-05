package com.cre.ojbackenduserservice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cre.ojbackendmodel.model.entity.SignInRecord;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SignInMapper extends BaseMapper<SignInRecord> {

}
