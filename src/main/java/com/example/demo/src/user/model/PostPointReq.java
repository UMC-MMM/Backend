package com.example.demo.src.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PostPointReq {
    private int userIdx;
    private boolean _isPlus;
    private int pointValue;
    private String pointContent;
}
