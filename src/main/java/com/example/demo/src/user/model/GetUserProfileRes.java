package com.example.demo.src.user.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetUserProfileRes {
    private int userIdx;
    private String profileImgUrl;
    private String userName;
    private String userGender;
    private int userAge;
    private String userEmail;
}
