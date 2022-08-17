package com.example.demo.src.survey.model;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import java.util.List;
@Getter
@Setter
@AllArgsConstructor
public class PostSurveyQuestionReq {
    private String questionType;
    private String questionContent;
    private List<PostSurveyQuestionOptionReq> questionOption;

}

