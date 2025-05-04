package com.cre.ojbackendquestionservice.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cre.ojbackendcommon.common.ErrorCode;
import com.cre.ojbackendcommon.exception.BusinessException;
import com.cre.ojbackendmodel.model.entity.Question;
import com.cre.ojbackendmodel.model.entity.QuestionThumb;
import com.cre.ojbackendmodel.model.entity.User;
import com.cre.ojbackendquestionservice.mapper.QuestionThumbMapper;
import com.cre.ojbackendquestionservice.service.QuestionService;
import com.cre.ojbackendquestionservice.service.QuestionThumbService;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.aop.framework.AopContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;


/**
 * 帖子点赞服务实现
 */
@Service
public class QuestionThumbServiceImpl extends ServiceImpl<QuestionThumbMapper, QuestionThumb> implements QuestionThumbService {

    @Resource
    private QuestionService questionService;


    @Resource
    private RedissonClient redissonClient;

    @Override
    public int doQuestionThumb(Long questionId, User loginUser) {
        // 判断文章是否存在
        Question question = questionService.getById(questionId);
        if (question == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        // 是否已点赞
        Long userId = loginUser.getId();
        // 每个用户串行点赞
        // 锁必须要包裹住事务方法
        String lockKey = "question_thumb:lock:" + userId; // 分布式锁键
        RLock lock = redissonClient.getLock(lockKey);
        try {
            // 尝试获取锁，等待时间5秒，锁持有时间30秒（自动续期）
            boolean isLocked = lock.tryLock(5, 30, TimeUnit.SECONDS);
            if (!isLocked) {
                throw new BusinessException(ErrorCode.OPERATION_ERROR, "操作过于频繁，请稍后再试");
            }
            // 通过代理调用事务方法
            QuestionThumbService proxyService = (QuestionThumbService) AopContext.currentProxy();
            return proxyService.doQuestionThumbInner(userId, questionId);
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
    @Transactional(rollbackFor = Exception.class)
    public int doQuestionThumbInner(Long userId, Long questionId) {
        QuestionThumb questionThumb = new QuestionThumb();
        questionThumb.setUserId(userId);
        questionThumb.setQuestionId(questionId);
        QueryWrapper<QuestionThumb> thumbQueryWrapper = new QueryWrapper<>(questionThumb);
        QuestionThumb oldQuestionThumb = this.getOne(thumbQueryWrapper);
        boolean result;
        // 已点赞
        if (oldQuestionThumb != null) {
            result = this.remove(thumbQueryWrapper);
            if (result) {
                // 点赞数 - 1
                result = questionService.update().eq("id", questionId).gt("thumb_num", 0).setSql("thumb_num = thumb_num - 1").update();
                return result ? -1 : 0;
            } else {
                throw new BusinessException(ErrorCode.SYSTEM_ERROR);
            }
        } else {
            // 未点赞
            result = this.save(questionThumb);
            if (result) {
                // 点赞数 + 1
                result = questionService.update().eq("id", questionId).setSql("thumb_num = thumb_num + 1").update();
                return result ? 1 : 0;
            } else {
                throw new BusinessException(ErrorCode.SYSTEM_ERROR);
            }
        }
    }

}




