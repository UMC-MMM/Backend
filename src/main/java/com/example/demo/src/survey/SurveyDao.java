package com.example.demo.src.survey;

import com.example.demo.src.survey.model.GetSurveyRes;
import com.example.demo.src.user.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class SurveyDao {

    private JdbcTemplate jdbcTemplate;
    private List<GetSurveyRes> getSurveyRes;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }



    public int deleteSurvey(int surveyIdx){
        String deleteSurveyQuery = "UPDATE Survey SET surveyStatus='N' WHERE surveyIdx=?";
        Object [] deleteSurveyParams = new Object[] {surveyIdx};

        return this.jdbcTemplate.update(deleteSurveyQuery, deleteSurveyParams);

    }

    public int checkUserExist(int userIdx){
        String checkUserExistQuery = "select exists(select userIdx from User where userIdx = ?)";
        int checkUserExistParams = userIdx;
        return this.jdbcTemplate.queryForObject(checkUserExistQuery,
                int.class,
                checkUserExistParams);

    }

}


