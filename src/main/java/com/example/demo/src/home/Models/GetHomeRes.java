package com.example.demo.src.home.Models;

import com.example.demo.src.survey.model.GetSurveyRes;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class GetHomeRes {
    private List<GetSurveyRes> bestSurvey;
    private List<String> surveyCategoryList;
}

