package com.example.demo.src.survey.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class PostSurveyReq {
    private int userIdx;
    private List<PostSurveyQuestionReq> surveyQuestionReqs;


}
