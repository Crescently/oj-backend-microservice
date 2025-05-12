package com.cre.ojbackendpostservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cre.ojbackendcommon.common.ErrorCode;
import com.cre.ojbackendcommon.exception.BusinessException;
import com.cre.ojbackendmodel.model.entity.Post;
import com.cre.ojbackendmodel.model.entity.PostFavour;
import com.cre.ojbackendmodel.model.entity.User;
import com.cre.ojbackendpostservice.mapper.PostFavourMapper;
import com.cre.ojbackendpostservice.service.PostFavourService;
import com.cre.ojbackendpostservice.service.PostService;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.aop.framework.AopContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * 帖子收藏服务实现
 */
@Service
public class PostFavourServiceImpl extends ServiceImpl<PostFavourMapper, PostFavour> implements PostFavourService {

    @Resource
    private PostService postService;
    @Resource
    private RedissonClient redissonClient;

    /**
     * 帖子收藏
     */
    @Override
    public int doPostFavour(long postId, User loginUser) {
        // 判断是否存在
        Post post = postService.getById(postId);
        if (post == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        // 是否已帖子收藏
        long userId = loginUser.getId();
        // 每个用户串行帖子收藏
        String lockKey = "post_favour:lock:" + userId; // 分布式锁键
        RLock lock = redissonClient.getLock(lockKey);
        try {
            // 尝试获取锁，等待时间5秒，锁持有时间30秒（自动续期）
            boolean isLocked = lock.tryLock(5, 30, TimeUnit.SECONDS);
            if (!isLocked) {
                throw new BusinessException(ErrorCode.OPERATION_ERROR, "操作过于频繁，请稍后再试");
            }
            // 通过代理调用事务方法
            PostFavourService proxyService = (PostFavourService) AopContext.currentProxy();
            return proxyService.doPostFavourInner(userId, postId);
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
    public Page<Post> listFavourPostByPage(IPage<Post> page, Wrapper<Post> queryWrapper, long userId) {
        if (userId <= 0) {
            return new Page<>();
        }
        return baseMapper.listFavourPostByPage(page, queryWrapper, userId);
    }

    /**
     * 封装了事务的方法
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int doPostFavourInner(long userId, long postId) {
        PostFavour postFavour = new PostFavour();
        postFavour.setUserId(userId);
        postFavour.setPostId(postId);
        QueryWrapper<PostFavour> postFavourQueryWrapper = new QueryWrapper<>(postFavour);
        PostFavour oldPostFavour = this.getOne(postFavourQueryWrapper);
        boolean result;
        // 已收藏
        if (oldPostFavour != null) {
            result = this.remove(postFavourQueryWrapper);
            if (result) {
                // 帖子收藏数 - 1
                result = postService.update().eq("id", postId).gt("favour_num", 0).setSql("favour_num = favour_num - 1").update();
                return result ? -1 : 0;
            } else {
                throw new BusinessException(ErrorCode.SYSTEM_ERROR);
            }
        } else {
            // 未帖子收藏
            result = this.save(postFavour);
            if (result) {
                // 帖子收藏数 + 1
                result = postService.update().eq("id", postId).setSql("favour_num = favour_num + 1").update();
                return result ? 1 : 0;
            } else {
                throw new BusinessException(ErrorCode.SYSTEM_ERROR);
            }
        }
    }

}




