package com.example.demo.config;

import lombok.Getter;

/**
 * 에러 코드 관리
 */
@Getter
public enum BaseResponseStatus {
    /**
     * 1000 : 요청 성공
     */
    SUCCESS(true, 1000, "요청에 성공하였습니다."),


    /**
     * 2000 : Request 오류
     */
    // Common
    REQUEST_ERROR(false, 2000, "입력값을 확인해주세요."),
    EMPTY_JWT(false, 2001, "JWT를 입력해주세요."),
    INVALID_JWT(false, 2002, "유효하지 않은 JWT입니다."),
    INVALID_USER_JWT(false,2003,"권한이 없는 유저의 접근입니다."),

    // users
    USERS_EMPTY_USER_ID(false, 2010, "유저 아이디 값을 확인해주세요."),
    USERS_EXISTS_USER_ID(false,2011,"중복된 아이디입니다."),
    // [POST] /users
    POST_USERS_EMPTY_EMAIL(false, 2015, "이메일을 입력해주세요."),
    POST_USERS_INVALID_EMAIL(false, 2016, "이메일 형식을 확인해주세요."),
    POST_USERS_EXISTS_EMAIL(false,2017,"중복된 이메일입니다."),

    POST_POSTS_INVALID_CONTENTS(false,2018,"게시글 내용의 글자 수가 허용된 범위를 초과하였습니다."),
    POST_POSTS_EMPTY_IMGURL(false,2019,"현재 게시글의 이미지가 없습니다."),
    POSTS_EMPTY_POST_ID(false, 2020, "게시글이 존재하지 않습니다. 게시글 아이디 값을 확인해주세요."),

    POST_USERS_EMPTY_PASSWORD(false, 2030, "비밀번호를 입력해주세요."),
    POST_USERS_INVALID_PASSWORD(false, 2031, "비밀번호 형식을 확인해주세요."),

    POST_SURVEY_EMPTY_TITLE(false, 2032, "제목을 입력하세요."),
    POST_SURVEY_INVALID_TITLE(false, 2033, "입력하신 제목의 글자 수가 허용된 범위를 초과하였습니다."),

    POST_SURVEY_EMPTY_DEADLINE(false, 2034,"마감일을 입력하세요."),
    POST_SURVEY_EMPTY_SURVEYTIME(false, 2035, "설문 소요 시간을 입력하세요."),
    POST_SURVEY_INVALID_INTRODUCTION(false, 2037, "입력하신 소개글의 글자 수가 허용된 범위를 초과하였습니다."),
    POST_SURVEY_EMPTY_QUESTION(false, 2038, "문항을 추가하세요."),
    POST_SURVEY_INVALID_HASHTAG(false, 2039,"입력하신 해시태그의 글자 수가 허용된 범위를 초과하였습니다."),
    POST_SURVEY_EMPTY_CONTENT(false,2040,"문항의 내용을 입력하세요."),
    POST_SURVEY_EMPTY_OPTION_CONTENT(false,2041,"선택 옵션 내용을 입력하세요."),
    POST_SURVEY_EMPTY_QUESTIONTYPE(false,2042,"문항 타입을 선택하세요."),


    /**
     * 3000 : Response 오류
     */
    // Common
    RESPONSE_ERROR(false, 3000, "값을 불러오는데 실패하였습니다."),

    // [POST] /users
    DUPLICATED_EMAIL(false, 3013, "중복된 이메일입니다."),
    FAILED_TO_LOGIN(false,3014,"없는 아이디거나 비밀번호가 틀렸습니다."),

    CHECK_ERROR(false,7777,"test중입니다"),

    /**
     * 4000 : Database, Server 오류
     */
    DATABASE_ERROR(false, 4000, "데이터베이스 연결에 실패하였습니다."),
    SERVER_ERROR(false, 4001, "서버와의 연결에 실패하였습니다."),

    //[PATCH] /users/{userIdx}
    MODIFY_FAIL_USERNAME(false,4014,"유저네임 수정 실패"),

    //Post /posts
    MODIFY_FAIL_POST(false,4015,"설문조사 내용 수정에 실패하였습니다."),
    DELETE_FAIL_POST(false,4016,"설문조사 삭제에 실패하였습니다."),
    PASSWORD_ENCRYPTION_ERROR(false, 4011, "비밀번호 암호화에 실패하였습니다."),
    PASSWORD_DECRYPTION_ERROR(false, 4012, "비밀번호 복호화에 실패하였습니다."),


    // 5000 : 필요시 만들어서 쓰세요

    TOTAL_POINT_MINUS(false, 5000, "포인트의 총합이 마이너스입니다.");

    // 6000 : 필요시 만들어서 쓰세요

    private final boolean isSuccess;
    private final int code;
    private final String message;

    private BaseResponseStatus(boolean isSuccess, int code, String message) {
        this.isSuccess = isSuccess;
        this.code = code;
        this.message = message;
    }
}
