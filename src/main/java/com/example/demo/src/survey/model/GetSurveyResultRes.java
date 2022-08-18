package com.example.demo.src.survey.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class GetSurveyResultRes {
    private GetSurveyRes getSurveyRes;
    private List<QuestionResult> questionResultList;
}
