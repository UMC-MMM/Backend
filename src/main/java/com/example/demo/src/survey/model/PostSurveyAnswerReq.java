package com.example.demo.src.survey.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class PostSurveyAnswerReq {
    private int userIdx; //답변자idx
    private int surveyIdx;
    private List<PostSurveyQuestionAnswerReq> surveyQuestionAnswerReqs;
}
