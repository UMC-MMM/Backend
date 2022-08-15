package com.example.demo.src.survey;

import com.example.demo.config.BaseException;
import com.example.demo.src.survey.model.GetSurveyRes;
import com.example.demo.src.user.UserDao;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;
import static com.example.demo.config.BaseResponseStatus.USERS_EMPTY_USER_ID;

@Service
public class SurveyProvider {
    private final SurveyDao surveyDao;
    private final JwtService jwtService;


    final Logger logger = LoggerFactory.getLogger(this.getClass());

    public SurveyProvider(SurveyDao surveyDao, JwtService jwtService) {
        this.surveyDao = surveyDao;
        this.jwtService = jwtService;
    }


    public List<GetSurveyRes> retrieveSurvey() throws BaseException{

        try{
            List<GetSurveyRes> getSurvey = surveyDao.selectSurvey();

            return getSurvey;
        }
        catch (Exception exception) {
            //System.out.println(exception);
            throw new BaseException(DATABASE_ERROR);
        }

    }

    public int checkUserExist(int userIdx) throws BaseException{
        try{
            return surveyDao.checkUserExist(userIdx);
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }


    public int checkSurveyExist(int surveyIdx) throws BaseException{
        try{
            return surveyDao.checkUserExist(surveyIdx);
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

}
