package com.cre.ojbackendmodel.model.vo;

import cn.hutool.json.JSONUtil;
import com.cre.ojbackendmodel.model.entity.Question;
import com.cre.ojbackendmodel.model.request.question.JudgeConfig;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;


@Data
public class QuestionVO implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * id
     */
    private Long id;
    /**
     * 标题
     */
    private String title;
    /**
     * 内容
     */
    private String content;
    /**
     * 标签列表（json 数组）
     */
    private List<String> tags;
    /**
     * 题目提交数
     */
    private Integer submitNum;
    /**
     * 题目通过数
     */
    private Integer acceptedNum;
    /**
     * 判题配置（json 对象）
     */
    private JudgeConfig judgeConfig;
    /**
     * 点赞数
     */
    private Integer thumbNum;
    /**
     * 收藏数
     */
    private Integer favourNum;
    /**
     * 是否点赞
     */
    private boolean isThumb;
    /**
     * 是否收藏
     */
    private boolean isFavour;
    /**
     * 创建用户 id
     */
    private Long userId;
    private UserVO userVO;
    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    /**
     * 包装类转对象
     */
    public static Question voToObj(QuestionVO questionVO) {
        if (questionVO == null) {
            return null;
        }
        Question question = new Question();
        BeanUtils.copyProperties(questionVO, question);
        List<String> tagList = questionVO.getTags();
        question.setTags(JSONUtil.toJsonStr(tagList));
        JudgeConfig judgeConfig = questionVO.getJudgeConfig();
        if (judgeConfig != null) {
            question.setJudgeCase(JSONUtil.toJsonStr(judgeConfig));
        }

        return question;
    }

    /**
     * 对象转包装类
     */
    public static QuestionVO objToVo(Question question) {
        if (question == null) {
            return null;
        }
        QuestionVO questionVO = new QuestionVO();
        BeanUtils.copyProperties(question, questionVO);
        questionVO.setTags(JSONUtil.toList(question.getTags(), String.class));

        String judgeConfig = question.getJudgeConfig();
        questionVO.setJudgeConfig(JSONUtil.toBean(judgeConfig, JudgeConfig.class));
        return questionVO;
    }
}