package com.cre.ojbackendquestionservice.service.Impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cre.ojbackendcommon.common.ErrorCode;
import com.cre.ojbackendcommon.exception.BusinessException;
import com.cre.ojbackendmodel.model.entity.Question;
import com.cre.ojbackendmodel.model.entity.QuestionFavour;
import com.cre.ojbackendmodel.model.entity.User;
import com.cre.ojbackendquestionservice.mapper.QuestionFavourMapper;
import com.cre.ojbackendquestionservice.service.QuestionFavourService;
import com.cre.ojbackendquestionservice.service.QuestionService;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.aop.framework.AopContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;


@Service
@Slf4j
public class QuestionFavourServiceImpl extends ServiceImpl<QuestionFavourMapper, QuestionFavour> implements QuestionFavourService {

    @Resource
    private QuestionService questionService;


    @Resource
    private RedissonClient redissonClient;

    /**
     * 问题收藏
     */
    @Override
    public int doQuestionFavour(Long questionId, User loginUser) {
        // 判断是否存在
        Question question = questionService.getById(questionId);
        if (question == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        // 是否已收藏
        Long userId = loginUser.getId();
        String lockKey = "question_favour:lock:" + userId; // 分布式锁键
        RLock lock = redissonClient.getLock(lockKey);
        try {
            // 尝试获取锁，等待时间5秒，锁持有时间30秒（自动续期）
            boolean isLocked = lock.tryLock(5, 30, TimeUnit.SECONDS);
            if (!isLocked) {
                throw new BusinessException(ErrorCode.OPERATION_ERROR, "操作过于频繁，请稍后再试");
            }
            // 通过代理调用事务方法
            QuestionFavourService proxyService = (QuestionFavourService) AopContext.currentProxy();
            return proxyService.doQuestionFavourInner(userId, questionId);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "获取锁失败");
        } finally {
            // 确保当前线程持有锁再释放
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }

    @Override
    public Page<Question> listFavourQuestionByPage(IPage<Question> page, Wrapper<Question> queryWrapper, Long userId) {
        if (userId == null) {
            return new Page<>();
        }
        log.info("用户id{}", userId);
        return baseMapper.listFavourQuestionByPage(page, queryWrapper, userId);
    }

    /**
     * 封装了事务的方法
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int doQuestionFavourInner(Long userId, Long questionId) {
        QuestionFavour questionFavour = new QuestionFavour();
        questionFavour.setUserId(userId);
        questionFavour.setQuestionId(questionId);
        QueryWrapper<QuestionFavour> questionFavourQueryWrapper = new QueryWrapper<>(questionFavour);
        QuestionFavour oldQuestionFavour = this.getOne(questionFavourQueryWrapper);
        boolean result;
        // 已收藏
        if (oldQuestionFavour != null) {
            result = this.remove(questionFavourQueryWrapper);
            if (result) {
                // 帖子收藏数 - 1
                result = questionService.update().eq("id", questionId).gt("favour_num", 0).setSql("favour_num = favour_num - 1").update();
                return result ? -1 : 0;
            } else {
                throw new BusinessException(ErrorCode.SYSTEM_ERROR);
            }
        } else {
            // 未帖子收藏
            result = this.save(questionFavour);
            if (result) {
                // 帖子收藏数 + 1
                result = questionService.update().eq("id", questionId).setSql("favour_num = favour_num + 1").update();
                return result ? 1 : 0;
            } else {
                throw new BusinessException(ErrorCode.SYSTEM_ERROR);
            }
        }
    }


}




