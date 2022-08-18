package com.example.demo.src.survey.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class QuestionResult {
    private String questionContent;
    private List<AnswerResult> answerResultList;
}
