package com.example.demo.src.user.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetPointRes {
    private int pointIdx;
    private int pointValue;
    private String createdAt;
    private String pointContent;
}
