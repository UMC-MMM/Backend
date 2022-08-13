package com.example.demo.src.survey.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetSurveyRes {
    private int surveyIdx;
    private String surveyTitle;
    private String createdAt;
    private String deadlineAt;
    private int writerId;
    private String preferGender;
    private int preferAge;
    private int surveyTime;
    private String hashtag;
    private int surveyCategoryIdx;
    private int surveyPointValue;
    private int totalParticipant;
    private int userIdx;
}
