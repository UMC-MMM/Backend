package com.example.demo.src.survey;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.config.BaseResponseStatus;

import com.example.demo.src.survey.model.*;

import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/survey")

public class SurveyController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final SurveyProvider surveyProvider;
    @Autowired
    private final SurveyService surveyService;

    @Autowired
    private final JwtService jwtService;


    public SurveyController(SurveyProvider surveyProvider, SurveyService surveyService, JwtService jwtService) {
        this.surveyProvider = surveyProvider;
        this.surveyService = surveyService;
        this.jwtService = jwtService;

    }


/*
설문조사 목록
 */
    @ResponseBody
    @GetMapping("") //(Get) 127.0.0.1:9000/survey
    public BaseResponse<List<GetSurveyRes>> getSurveys() { //여러 게시글 볼 수 있으므로 list, 따로 받을 값 없으므로 파라미터 비움
        try {
            List<GetSurveyRes> getSurveyRes = surveyProvider.retrieveSurvey();
            return new BaseResponse<>(getSurveyRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

/*
설문조사 등록
 */
    @ResponseBody

    @PostMapping("") //(Post) 127.0.0.1:9000/survey
    public BaseResponse<PostSurveyRes> createSurvey(@RequestBody PostSurveyReq postSurveyReq) {
        try{
            int userIdxByJwt = jwtService.getUserIdx();
            if(postSurveyReq.getUserIdx()!=userIdxByJwt){
                return new BaseResponse<>(BaseResponseStatus.INVALID_USER_JWT);
            }

/*
            if(postSurveyQuestionReq.getQuestionType().length()<1)
            {
                return new BaseResponse<>(BaseResponseStatus.POST_SURVEY_EMPTY_QUESTIONTYPE);
            }
*/

            if(postSurveyReq.getSurveyIntroduction().length()>300)
            {
                return new BaseResponse<>(BaseResponseStatus.POST_SURVEY_INVALID_INTRODUCTION);
            }

            if(postSurveyReq.getSurveyTitle().length()<1)
            {
                return new BaseResponse<>(BaseResponseStatus.POST_SURVEY_EMPTY_TITLE);
            }
            if(postSurveyReq.getSurveyTitle().length()>30)
            {
                return new BaseResponse<>(BaseResponseStatus.POST_SURVEY_INVALID_TITLE);
            }
            if(postSurveyReq.getDeadlineAt().length()<1)
            {
                return new BaseResponse<>(BaseResponseStatus.POST_SURVEY_EMPTY_DEADLINE);
            }
            if(postSurveyReq.getSurveyTime()<1)
            {
                return new BaseResponse<>(BaseResponseStatus.POST_SURVEY_EMPTY_SURVEYTIME);
            }

            if(postSurveyReq.getHashtag().length()>45)
            {
                return new BaseResponse<>(BaseResponseStatus.POST_SURVEY_INVALID_HASHTAG);
            }

            if(postSurveyReq.getSurveyQuestion().size()<1)
            {
                return new BaseResponse<>(BaseResponseStatus.POST_SURVEY_EMPTY_QUESTION);
            }

            PostSurveyRes postSurveyRes = surveyService.createSurvey(postSurveyReq.getUserIdx(),postSurveyReq);
            return new BaseResponse<>(postSurveyRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /*
    설문조사 삭제
    */
    @ResponseBody
    @PatchMapping("/{surveyIdx}/status") //(Patch) 127.0.0.1:9000/survey/{surveyIdx}/status
    public BaseResponse<String> deleteSurvey(@PathVariable("surveyIdx") int surveyIdx) {
        try{

            surveyService.deleteSurvey(surveyIdx);
            String result = "게시글 삭제에 성공했습니다.";
            return new BaseResponse<>(result);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }
    /*
    설문조사 조회
     */
    @ResponseBody
    @GetMapping("/{surveyIdx}")//(GET) 127.0.0.1:9000/survey/{surveyIdx}
    public BaseResponse<GetSurvey> getSurvey(@PathVariable("surveyIdx") int surveyIdx) {
        try {
            GetSurvey getSurvey = surveyProvider.getSurvey(surveyIdx);
            return new BaseResponse<>(getSurvey);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }
    /*
    설문조사 답변 등록
     */
    @ResponseBody
    @PostMapping("/{surveyIdx}") // (Post) 127.0.0.1:9000/survey/{surveyIdx}
    public BaseResponse<String> createSurveyAnswer(@PathVariable("surveyIdx") int surveyIdx, @RequestBody PostSurveyAnswerReq postSurveyAnswerReq) {
        try{
            int userIdxByJwt = jwtService.getUserIdx();
            if(postSurveyAnswerReq.getUserIdx()!=userIdxByJwt){
                return new BaseResponse<>(BaseResponseStatus.INVALID_USER_JWT);
            }

            BaseResponse<String> result = surveyService.createSurveyAnswer(postSurveyAnswerReq.getUserIdx(),surveyIdx,postSurveyAnswerReq);
            return result;
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }


}
