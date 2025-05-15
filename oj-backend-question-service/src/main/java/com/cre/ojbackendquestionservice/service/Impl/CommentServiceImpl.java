package com.cre.ojbackendquestionservice.service.Impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cre.ojbackendcommon.constant.CommonConstant;
import com.cre.ojbackendcommon.utils.SqlUtils;
import com.cre.ojbackendmodel.model.entity.Comment;
import com.cre.ojbackendmodel.model.entity.User;
import com.cre.ojbackendmodel.model.request.comment.CommentAddRequest;
import com.cre.ojbackendmodel.model.request.comment.CommentQueryRequest;
import com.cre.ojbackendmodel.model.vo.CommentVO;
import com.cre.ojbackendquestionservice.mapper.CommentMapper;
import com.cre.ojbackendquestionservice.service.CommentService;
import com.cre.ojbackendserviceclient.service.UserFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


@Service
@Slf4j
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

    @Resource
    private CommentMapper commentMapper;

    @Resource
    private UserFeignClient userFeignClient;

    @Override
    public void addComment(CommentAddRequest commentAddRequest, long userId) {
        String content = commentAddRequest.getContent();
        Long questionId = commentAddRequest.getQuestionId();
        Comment comment = Comment.builder().questionId(questionId).content(content).userId(userId).build();
        commentMapper.insert(comment);
    }


    @Override
    public Wrapper<Comment> getQueryWrapper(CommentQueryRequest commentQueryRequest) {
        QueryWrapper<Comment> queryWrapper = new QueryWrapper<>();
        if (commentQueryRequest == null) {
            return queryWrapper;
        }
        Long questionId = commentQueryRequest.getQuestionId();
        String sortField = commentQueryRequest.getSortField();
        String sortOrder = commentQueryRequest.getSortOrder();

        // 拼接查询条件
        queryWrapper.eq(ObjectUtils.isNotEmpty(questionId), "question_id", questionId);
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC), sortField);
        return queryWrapper;
    }

    @Override
    public Page<CommentVO> getCommentVOPage(Page<Comment> commentPage, HttpServletRequest request) {
        List<Comment> commentList = commentPage.getRecords();
        Page<CommentVO> commentVOPage = new Page<>(commentPage.getCurrent(), commentPage.getSize(), commentPage.getTotal());
        if (CollUtil.isEmpty(commentList)) {
            return commentVOPage;
        }

        Set<Long> userIdSet = commentList.stream().map(Comment::getUserId).collect(Collectors.toSet());
        Map<Long, List<User>> userIdUserListMap = userFeignClient.listByIds(userIdSet).stream().collect(Collectors.groupingBy(User::getId));

        // 3. 填充数据
        List<CommentVO> commentVOList = commentList.stream().map(comment -> {
            Long userId = comment.getUserId();
            User user = null;
            if (userIdUserListMap.containsKey(userId)) {
                user = userIdUserListMap.get(userId).get(0);
            }
            return CommentVO.builder().id(comment.getId()).content(comment.getContent()).userVO(userFeignClient.getUserVO(user)).createTime(comment.getCreateTime()).build();
        }).collect(Collectors.toList());
        commentVOPage.setRecords(commentVOList);
        return commentVOPage;
    }
}
