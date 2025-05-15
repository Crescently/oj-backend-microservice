package com.cre.ojbackendmodel.model.request.comment;

import lombok.Data;

import java.io.Serializable;

/**
 * 添加评论请求
 */
@Data
public class CommentAddRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    private String content;

    private Long questionId;
}
