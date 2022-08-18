package com.example.demo.src.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PatchUserProfileReq {
    private int userIdx;
    private String profileImgUrl;
    private String userName;
    private String userGender;
    private int userAge;
    private String userEmail;
}
