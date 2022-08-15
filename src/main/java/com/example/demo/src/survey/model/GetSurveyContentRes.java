package com.example.demo.src.survey.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetSurveyContentRes {
    private int surveyIdx;
    private String surveyTitle;
    private String createdAt;
    private String deadlineAt;
    private String preferGender;
    private int preferAge;
    private int surveyTime;
    private String hashtag;
    private int surveyCategoryIdx;
    private int surveyPointValue;
    private int totalParticipant;
    private int userIdx;
}
