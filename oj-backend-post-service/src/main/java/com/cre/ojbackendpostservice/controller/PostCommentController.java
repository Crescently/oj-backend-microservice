package com.cre.ojbackendpostservice.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cre.ojbackendcommon.common.BaseResponse;
import com.cre.ojbackendcommon.common.ErrorCode;
import com.cre.ojbackendcommon.exception.BusinessException;
import com.cre.ojbackendmodel.model.entity.PostComment;
import com.cre.ojbackendmodel.model.entity.User;
import com.cre.ojbackendmodel.model.request.postcomment.PostCommentAddRequest;
import com.cre.ojbackendmodel.model.request.postcomment.PostCommentQueryRequest;
import com.cre.ojbackendmodel.model.vo.PostCommentVO;
import com.cre.ojbackendpostservice.service.PostCommentService;
import com.cre.ojbackendserviceclient.service.UserFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 帖子评论接口
 */
@RestController
@RequestMapping("/comment")
@Slf4j
public class PostCommentController {

    @Resource
    private PostCommentService postCommentService;

    @Resource
    private UserFeignClient userFeignClient;

    @PostMapping("/add")
    public BaseResponse addPostComment(@RequestBody PostCommentAddRequest postCommentAddRequest, HttpServletRequest request) {
        if (postCommentAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userFeignClient.getLoginUser(request);
        postCommentService.addPostComment(postCommentAddRequest, loginUser.getId());
        return BaseResponse.success();
    }


    @PostMapping("/list/page/vo")
    public BaseResponse<Page<PostCommentVO>> listPostCommentVOByPage(@RequestBody PostCommentQueryRequest postCommentQueryRequest, HttpServletRequest request) {
        long current = postCommentQueryRequest.getCurrent();
        long size = postCommentQueryRequest.getPageSize();
        Page<PostComment> commentPage = postCommentService.page(new Page<>(current, size), postCommentService.getQueryWrapper(postCommentQueryRequest));
        return BaseResponse.success(postCommentService.getPostCommentVOPage(commentPage, request));
    }
}
