package com.cre.ojbackendpostservice.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cre.ojbackendmodel.model.entity.PostComment;
import com.cre.ojbackendmodel.model.request.postcomment.PostCommentAddRequest;
import com.cre.ojbackendmodel.model.request.postcomment.PostCommentQueryRequest;
import com.cre.ojbackendmodel.model.vo.PostCommentVO;

import javax.servlet.http.HttpServletRequest;

/**
 * 帖子评论服务
 */
public interface PostCommentService extends IService<PostComment> {
    /**
     * 添加评论
     */
    Integer addPostComment(PostCommentAddRequest postCommentAddRequest, long userId);

    /**
     * 获取查询条件
     */
    Wrapper<PostComment> getQueryWrapper(PostCommentQueryRequest postCommentQueryRequest);

    /**
     * 获取评论对象分页（脱敏）
     */
    Page<PostCommentVO> getPostCommentVOPage(Page<PostComment> commentPage, HttpServletRequest request);

}
