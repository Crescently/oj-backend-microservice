package com.cre.ojbackendpostservice.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cre.ojbackendmodel.model.entity.PostThumb;
import com.cre.ojbackendmodel.model.entity.User;

/**
 * 帖子点赞服务
 */
public interface PostThumbService extends IService<PostThumb> {

    /**
     * 点赞
     */
    int doPostThumb(long postId, User loginUser);

    /**
     * 帖子点赞（内部服务）
     */
    int doPostThumbInner(long userId, long postId);
}
