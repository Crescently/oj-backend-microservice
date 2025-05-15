package com.cre.ojbackendmodel.model.request.comment;


import com.cre.ojbackendcommon.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 评论查询请求
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CommentQueryRequest extends PageRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long questionId;
}
