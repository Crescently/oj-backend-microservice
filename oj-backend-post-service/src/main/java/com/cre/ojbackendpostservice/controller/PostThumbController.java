package com.cre.ojbackendpostservice.controller;

import com.cre.ojbackendcommon.common.BaseResponse;
import com.cre.ojbackendcommon.common.ErrorCode;
import com.cre.ojbackendcommon.exception.BusinessException;
import com.cre.ojbackendmodel.model.entity.User;
import com.cre.ojbackendmodel.model.request.postthumb.PostThumbAddRequest;
import com.cre.ojbackendpostservice.service.PostThumbService;
import com.cre.ojbackendserviceclient.service.UserFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 帖子点赞接口
 */
@RestController
@RequestMapping("/thumb")
@Slf4j
public class PostThumbController {

    @Resource
    private PostThumbService postThumbService;

    @Resource
    private UserFeignClient userFeignClient;

    /**
     * 点赞 / 取消点赞
     */
    @PostMapping("/")
    public BaseResponse<Integer> doPostThumb(@RequestBody PostThumbAddRequest postThumbAddRequest,
                                         HttpServletRequest request) {
        if (postThumbAddRequest == null || postThumbAddRequest.getPostId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 登录才能点赞
        final User loginUser = userFeignClient.getLoginUser(request);
        long postId = postThumbAddRequest.getPostId();
        int result = postThumbService.doPostThumb(postId, loginUser);
        return BaseResponse.success(result);
    }

}
