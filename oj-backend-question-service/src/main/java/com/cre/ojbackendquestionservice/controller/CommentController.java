package com.cre.ojbackendquestionservice.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cre.ojbackendcommon.common.BaseResponse;
import com.cre.ojbackendcommon.common.ErrorCode;
import com.cre.ojbackendcommon.exception.BusinessException;
import com.cre.ojbackendmodel.model.entity.Comment;
import com.cre.ojbackendmodel.model.entity.User;
import com.cre.ojbackendmodel.model.request.comment.CommentAddRequest;
import com.cre.ojbackendmodel.model.request.comment.CommentQueryRequest;
import com.cre.ojbackendmodel.model.vo.CommentVO;
import com.cre.ojbackendquestionservice.service.CommentService;
import com.cre.ojbackendserviceclient.service.UserFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 问题评论接口
 */
@RestController
@RequestMapping("/comment")
@Slf4j
public class CommentController {

    @Resource
    private CommentService commentService;

    @Resource
    private UserFeignClient userFeignClient;

    /**
     * 添加评论
     */
    @PostMapping("/add")
    public BaseResponse<?> addComment(@RequestBody CommentAddRequest commentAddRequest, HttpServletRequest request) {
        if (commentAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userFeignClient.getLoginUser(request);
        commentService.addComment(commentAddRequest, loginUser.getId());
        return BaseResponse.success();
    }

    /**
     * 分页获取评论
     */
    @PostMapping("/list/page/vo")
    public BaseResponse<Page<CommentVO>> listCommentVOByPage(@RequestBody CommentQueryRequest commentQueryRequest, HttpServletRequest request) {
        long current = commentQueryRequest.getCurrent();
        long size = commentQueryRequest.getPageSize();
        Page<Comment> commentPage = commentService.page(new Page<>(current, size), commentService.getQueryWrapper(commentQueryRequest));
        return BaseResponse.success(commentService.getCommentVOPage(commentPage, request));
    }
}
