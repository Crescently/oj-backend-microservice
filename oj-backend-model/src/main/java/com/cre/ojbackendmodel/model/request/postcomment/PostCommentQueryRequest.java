package com.cre.ojbackendmodel.model.request.postcomment;

import com.cre.ojbackendcommon.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 帖子评论查询请求
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class PostCommentQueryRequest extends PageRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long postId;
}
