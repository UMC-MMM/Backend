package com.example.demo.src.survey;

import com.example.demo.config.BaseException;
import com.example.demo.src.survey.model.GetSurvey;
import com.example.demo.src.survey.model.GetSurveyQuestionRes;
import com.example.demo.src.survey.model.GetSurveyRes;
import com.example.demo.src.user.UserDao;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;

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
         //   System.out.println(exception);
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
            return surveyDao.checkSurveyExist(surveyIdx);
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public GetSurvey getSurvey(int surveyIdx)throws BaseException{
        try{
            if(checkSurveyExist(surveyIdx)==0){
                throw new BaseException(SURVEY_NOT_EXIST);
            }
            GetSurveyRes getSurveyRes =  surveyDao.selectSurveyOne(surveyIdx);
            List<GetSurveyQuestionRes> getSurveyQuestionRes = surveyDao.selectSurveyQuestions(surveyIdx);
            GetSurvey getSurvey = new GetSurvey(getSurveyRes, getSurveyQuestionRes);
            return getSurvey;
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }
    public  List<GetSurveyRes> getMySurvey(int userIdx) throws BaseException{
        try{
            if(checkUserExist(userIdx)==0){
                throw new BaseException(USERS_EMPTY_USER_ID);
            }
            System.out.println("ok!!!!!!!!!");
            List<GetSurveyRes> getMySurvey = surveyDao.selectSurveyByUserIdx(userIdx);
            System.out.println("ok2!!!!!!!!!");
            return  getMySurvey;
        }catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

}
