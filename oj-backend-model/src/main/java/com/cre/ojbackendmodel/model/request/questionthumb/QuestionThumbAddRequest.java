package com.cre.ojbackendmodel.model.request.questionthumb;

import lombok.Data;


import java.io.Serializable;

@Data
public class QuestionThumbAddRequest implements Serializable {


    private static final long serialVersionUID = 1L;


    private Long questionId;
}