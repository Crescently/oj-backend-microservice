package com.cre.ojbackendmodel.model.request.postcomment;

import lombok.Data;

import java.io.Serializable;

/**
 * 帖子评论添加请求
 */
@Data
public class PostCommentAddRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    private String content;

    private Long postId;
}
