package com.cre.ojbackendquestionservice.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cre.ojbackendmodel.model.entity.Comment;
import com.cre.ojbackendmodel.model.request.comment.CommentAddRequest;
import com.cre.ojbackendmodel.model.request.comment.CommentQueryRequest;
import com.cre.ojbackendmodel.model.vo.CommentVO;

import javax.servlet.http.HttpServletRequest;


public interface CommentService extends IService<Comment> {
    /**
     * 添加评论
     */
    void addComment(CommentAddRequest commentAddRequest, long userId);

    /**
     * 获取评论对象查询条件
     */
    Wrapper<Comment> getQueryWrapper(CommentQueryRequest commentQueryRequest);

    /**
     * 获取评论对象分页（脱敏）
     */
    Page<CommentVO> getCommentVOPage(Page<Comment> commentPage, HttpServletRequest request);
}
