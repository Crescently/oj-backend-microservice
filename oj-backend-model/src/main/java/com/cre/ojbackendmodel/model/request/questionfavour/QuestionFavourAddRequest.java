package com.cre.ojbackendmodel.model.request.questionfavour;

import lombok.Data;

import java.io.Serializable;


@Data
public class QuestionFavourAddRequest implements Serializable {


    private static final long serialVersionUID = 1L;


    private Long questionId;
}