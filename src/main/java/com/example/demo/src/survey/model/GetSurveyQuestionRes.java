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
public class GetSurveyQuestionRes {
    private int questionIdx;
    private String questionType;
    private String questionContent;
    private List<GetSurveyQuestionOptionRes> Options;
}
