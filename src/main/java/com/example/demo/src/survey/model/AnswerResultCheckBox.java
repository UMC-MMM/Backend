package com.example.demo.src.survey.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AnswerResultCheckBox implements AnswerResult{
    private String optionContent;
    private int count;
}
