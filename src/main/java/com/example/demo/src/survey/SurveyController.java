package com.example.demo.src.survey;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.example.demo.src.survey.model.GetSurveyRes;
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



    @ResponseBody
    @GetMapping("")
    public BaseResponse<List<GetSurveyRes>> getSurvey() { //여러 설문조사 볼 수 있으므로 list
        try {
            List<GetSurveyRes> getSurveyRes = surveyProvider.retrieveSurvey();
            return new BaseResponse<>(getSurveyRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }



    @ResponseBody
    @PatchMapping("/{surveyIdx}/status") // (GET) 127.0.0.1:9000/users
    public BaseResponse<String> deleteSurvey(@PathVariable("surveyIdx") int surveyIdx) {
        try{

            surveyService.deleteSurvey(surveyIdx);
            String result = "삭제를 성공했습니다.";
            return new BaseResponse<>(result);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

}
