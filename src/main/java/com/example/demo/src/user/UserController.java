package com.example.demo.src.user;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;

import com.example.demo.src.survey.SurveyProvider;
import com.example.demo.src.survey.model.GetSurvey;
import com.example.demo.src.survey.model.GetSurveyRes;
import com.example.demo.src.survey.model.GetSurveyResultRes;
import com.example.demo.src.user.model.*;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;
import static com.example.demo.utils.ValidationRegex.isRegexEmail;


@RestController
@RequestMapping("/users")
public class UserController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final UserProvider userProvider;
    @Autowired
    private final UserService userService;
    @Autowired
    private final JwtService jwtService;
    @Autowired
    private final SurveyProvider surveyProvider;

    public UserController(UserProvider userProvider, UserService userService, JwtService jwtService, SurveyProvider surveyProvider) {
        this.userProvider = userProvider;
        this.userService = userService;
        this.jwtService = jwtService;
        this.surveyProvider = surveyProvider;
    }

    /**
     * 회원가입 api
     */
    @ResponseBody
    @PostMapping("/join")
    public BaseResponse<PostUserRes> createUser(@RequestBody PostUserReq postUserReq) {
        if (postUserReq.getUserId() == null || postUserReq.getUserId().equals("")) {
            return new BaseResponse<>(USERS_EMPTY_USER_ID);
            //아이디 빈칸
        }
        if (postUserReq.getUserEmail() == null || postUserReq.getUserEmail().equals("")) {
            return new BaseResponse<>(POST_USERS_EMPTY_EMAIL);
            //이메일 빈칸
        }
        if (postUserReq.getUserPassword() == null || postUserReq.getUserPassword().equals("")) {
            return new BaseResponse<>(POST_USERS_EMPTY_PASSWORD);
            //비밀번호 빈칸
        }
        if (!isRegexEmail(postUserReq.getUserEmail())) {
            return new BaseResponse<>(POST_USERS_INVALID_EMAIL);
            //이메일 정규표현 검사
        }
        try {
            PostUserRes postUserRes = userService.createUser(postUserReq);
            return new BaseResponse<>(postUserRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }

    }

    @ResponseBody
    @PostMapping("/login")
    public BaseResponse<PostLoginRes> login(@RequestBody PostLoginReq postLoginReq) {
        try {
            if (postLoginReq.getId() == null || postLoginReq.getId().equals("")) {
                return new BaseResponse<>(USERS_EMPTY_USER_ID);
            }
            if (postLoginReq.getPassword() == null || postLoginReq.getPassword().equals("")) {
                return new BaseResponse<>(POST_USERS_EMPTY_PASSWORD);
            }
            PostLoginRes postLoginRes = userService.login(postLoginReq);
            return new BaseResponse<>(postLoginRes);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /*
    유저 프로필 조회
    */
    @ResponseBody
    @GetMapping("/{userIdx}/profile") //(Get) 127.0.0.1:9000/users/{userIdx}/profile
    public BaseResponse<GetUserProfileRes> getUserProfile(@PathVariable ("userIdx") int userIdx) {

        try {
            GetUserProfileRes getUserProfileRes = userProvider.getUserProfile(userIdx);
            return new BaseResponse<>(getUserProfileRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /*
    유저 프로필 수정
     */
    @ResponseBody
    @PatchMapping("/{userIdx}/profile-modification") // (Patch) 127.0.0.1:9000/users/{userIdx}/profile-modification
    public BaseResponse<String> modifyUserProfile(@PathVariable("userIdx") int userIdx, @RequestBody PatchUserProfileReq patchUserProfileReq) {
        try{
            //jwt에서 idx 추출
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if(userIdx != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            if (patchUserProfileReq.getUserName() == null || patchUserProfileReq.getUserName().equals("")) {
                return new BaseResponse<>(USERS_EMPTY_USER_NAME);
                //유저 이름(닉네임) 빈칸
            }

            if (patchUserProfileReq.getUserEmail() == null || patchUserProfileReq.getUserEmail().equals("")) {
                return new BaseResponse<>(POST_USERS_EMPTY_EMAIL);
                //이메일 빈칸
            }

            if (!isRegexEmail(patchUserProfileReq.getUserEmail())) {
                return new BaseResponse<>(POST_USERS_INVALID_EMAIL);
                //이메일 정규표현 검사
            }
            userService.modifyUserProfile(userIdx, patchUserProfileReq);
            String result = "유저 프로필 변경을 성공하였습니다.";

            return new BaseResponse<>(result);
        } catch(BaseException exception){
            System.out.println(exception);
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 내 설문조사 리스트 조회 api
     */
    @ResponseBody
    @GetMapping("/mysurveys")
    public BaseResponse<List<GetSurveyRes>> getMySurveys() {
        try {
            int userIdxByJwt = jwtService.getUserIdx();
            List<GetSurveyRes> getSurveyRes = surveyProvider.getMySurvey(userIdxByJwt);
            return new BaseResponse<>(getSurveyRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }
    /*
    설문조사 결과 조회
     */
    @ResponseBody
    @GetMapping("/mysurveys/{surveyIdx}")
    public BaseResponse<GetSurveyResultRes> getMySurveyResult(@PathVariable int surveyIdx) {
        try {
            int userIdxByJwt = jwtService.getUserIdx();
            if(!surveyProvider.isMySurvey(userIdxByJwt,surveyIdx)){
                return new BaseResponse<>(NOT_USERS_SURVEY);
            }
            GetSurveyResultRes getSurveyResultRes = surveyProvider.getSurveyResult(surveyIdx);
            return new BaseResponse<>(getSurveyResultRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }


    /**
     * 포인트 조회 api
     */
    @ResponseBody
    @GetMapping("/point")
    public BaseResponse<GetUserPointRes> getUserPoint()  {
        try {
            int userIdxByJwt = jwtService.getUserIdx();
            GetUserPointRes getUserPointRes = userProvider.getUserPoint(userIdxByJwt);
            return new BaseResponse<>(getUserPointRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }

    }
}










