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
    private int surveyCategoryIdx;

    private String deadlineAt;
    private String surveyTitle;
    private List<PostSurveyQuestionReq> surveyQuestion;

    private String preferGender;
    private int preferAge;
    private int surveyTime;
    private String hashtag;
    private int surveyPointValue;
    private int couponIdx;
}
