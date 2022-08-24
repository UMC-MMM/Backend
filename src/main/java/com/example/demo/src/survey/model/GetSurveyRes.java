package com.example.demo.src.survey.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetSurveyRes {
    private int surveyIdx;
    private String surveyTitle;
    private String introduction;
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
    private String userName;
}
