package com.example.demo.src.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PostUserReq {
    private String userId;
    private String userPassword;
    private String userName;
    private String userEmail;

}
//userID, userName,userEmail, userPassword
