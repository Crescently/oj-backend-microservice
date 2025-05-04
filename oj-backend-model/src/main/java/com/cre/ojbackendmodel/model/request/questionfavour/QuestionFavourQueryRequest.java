package com.cre.ojbackendmodel.model.request.questionfavour;


import com.cre.ojbackendcommon.common.PageRequest;
import com.cre.ojbackendmodel.model.request.question.QuestionQueryRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = true)
public class QuestionFavourQueryRequest extends PageRequest implements Serializable {


    private static final long serialVersionUID = 1L;
    /**
     * 文章查询请求
     */
    private QuestionQueryRequest questionQueryRequest;
    /**
     * 用户 id
     */
    private Long userId;
}