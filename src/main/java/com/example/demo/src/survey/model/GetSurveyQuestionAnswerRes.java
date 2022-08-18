package com.example.demo.src.survey.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class GetSurveyQuestionAnswerRes {
    private String questionType; // 서술형 / 체크박스
    private String questionContent; //질문 내용
    private List<AnswerResult> answerResultList; // 답변 결과
}