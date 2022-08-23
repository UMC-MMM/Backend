package com.example.demo.src.survey;

import com.example.demo.config.BaseException;
import com.example.demo.src.survey.model.*;
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
            System.out.println(exception);
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

    /*
    설문조사 존재 여부 존재하면 1 없으면 0
     */
    public int checkSurveyExist(int surveyIdx) throws BaseException{
        try{
            return surveyDao.checkSurveyExist(surveyIdx);
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }
    /*
    유효한 설문조사인지(마감안됐는지)
    유효 2 마감 1 삭제 0
     */
    public int checkSurveyIsValid(int surveyIdx) throws BaseException{
         try{
             return surveyDao.checkSurveyIsValid(surveyIdx);
         }catch(Exception exception) {
             throw new BaseException(DATABASE_ERROR);
         }
    }

    /*
    내 설문조사인지
    */
    public boolean isMySurvey(int userIdxByJwt, int surveyIdx) throws BaseException{
        try{
            return surveyDao.checkMySurvey(userIdxByJwt,surveyIdx);
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    /*
    설문조사Idx로 설문조사 조회
     */
    public GetSurvey getSurvey(int surveyIdx) throws BaseException{
        if(checkSurveyExist(surveyIdx)==0){
            throw new BaseException(SURVEY_NOT_EXIST);
        }
        if(checkSurveyIsValid(surveyIdx)==0){
            throw new BaseException(SURVEY_IS_DELETED);
        }
        try{
            GetSurveyRes getSurveyRes =  surveyDao.selectSurveyOne(surveyIdx);
            List<GetSurveyQuestionRes> getSurveyQuestionRes = surveyDao.selectSurveyQuestions(surveyIdx);
            GetSurvey getSurvey = new GetSurvey(getSurveyRes, getSurveyQuestionRes);
            return getSurvey;
        } catch (Exception exception){
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }
    /*
    userIdx로 내설문조사 리스트 조회
     */
    public  List<GetSurveyRes> getMySurvey(int userIdx) throws BaseException{
        try{
            if(checkUserExist(userIdx)==0){
                throw new BaseException(USERS_EMPTY_USER_ID);
            }
            List<GetSurveyRes> getSurvey = surveyDao.selectSurveyByUserIdx(userIdx);
            return getSurvey;
        }catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }
    /*
    내설문조사 결과 조회
     */
    public GetSurveyResultRes getSurveyResult(int surveyIdx) throws BaseException{
        if(checkSurveyIsValid(surveyIdx)==0){
            throw new BaseException(SURVEY_IS_DELETED);
        }
        try{
            GetSurveyRes getSurveyRes = surveyDao.selectSurveyOne(surveyIdx);
            List<QuestionResult> questionResultList = surveyDao.selectQuestionResult(surveyIdx);
            return new GetSurveyResultRes(getSurveyRes,questionResultList);
        }catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    /*
    설문조사에 이미 참여한 유저인지
     */
    public boolean isParticipatedUser(int userIdx, int surveyIdx) throws  BaseException{
        try{
            boolean result = surveyDao.checkParticipatedUser(userIdx, surveyIdx);
            return result;
        }catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

}
