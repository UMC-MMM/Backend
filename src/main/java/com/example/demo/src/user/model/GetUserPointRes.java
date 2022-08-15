package com.example.demo.src.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class GetUserPointRes {
    private int userIdx;
    private int totalPoint;
    private List<GetPointRes> plusPointHistory;
    private List<GetPointRes> MinusPointHistory;

}
