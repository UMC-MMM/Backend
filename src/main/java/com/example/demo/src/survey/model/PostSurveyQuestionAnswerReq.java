package com.example.demo.src.survey.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class PostSurveyQuestionAnswerReq {
    private int questionIdx;
    private String questionAnswer;//주관식이면 String 타입의 답변 ,  객관식이면 String "[checkedOptionAnswerIdx, checkedOptionAnswerIdx]"
}

