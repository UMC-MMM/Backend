package com.example.demo.src.survey;

import com.example.demo.config.BaseException;
import com.example.demo.src.user.UserDao;
import com.example.demo.src.user.UserProvider;
import com.example.demo.src.user.model.*;
import com.example.demo.utils.JwtService;
import com.example.demo.utils.SHA256;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import static com.example.demo.config.BaseResponseStatus.*;

@Service
public class SurveyService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final SurveyDao surveyDao;
    private final SurveyProvider surveyProvider;
    private final JwtService jwtService;


    public SurveyService(SurveyDao surveyDao, SurveyProvider surveyProvider, JwtService jwtService) {
        this.surveyDao = surveyDao;
        this.surveyProvider = surveyProvider;
        this.jwtService = jwtService;

    }
    public void deleteSurvey(int surveyIdx) throws BaseException{

        try{
            int result = surveyDao.deleteSurvey(surveyIdx);
            if(result == 0){
                throw new BaseException(MODIFY_FAIL_POST);
            }
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }

    }

}
