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
    private String surveyIntroduction;
    private String surveyTitle;
    private int surveyCategoryIdx;
    private List<PostSurveyQuestionReq> surveyQuestion;
    private String deadlineAt;
    private String preferGender;
    private int preferAge;
    private int surveyTime;
    private String hashtag;
    private int surveyPointValue;
    private int couponIdx;
}
