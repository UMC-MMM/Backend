package com.example.demo.src.home;

import com.example.demo.config.BaseException;
import com.example.demo.src.home.Models.GetHomeRes;
import com.example.demo.src.survey.SurveyDao;
import com.example.demo.src.survey.model.GetSurveyRes;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;

@Service
public class HomeProvider {
    private final SurveyDao surveyDao;
    private final JwtService jwtService;

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    public HomeProvider(SurveyDao surveyDao, JwtService jwtService) {
        this.surveyDao = surveyDao;
        this.jwtService = jwtService;
    }

    public GetHomeRes getHome() throws BaseException {
        try {
            List<GetSurveyRes> getSurvey = surveyDao.selectSurvey();
            Map<Integer,String> getCategoryList = surveyDao.selectSurveyCategoryList();
            GetHomeRes getHomeRes = new GetHomeRes(getSurvey, getCategoryList);
            return getHomeRes;

        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
