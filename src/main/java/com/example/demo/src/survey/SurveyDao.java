package com.example.demo.src.survey;

import com.example.demo.src.survey.model.GetSurveyRes;
import com.example.demo.src.survey.model.PostSurveyQuestionReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Repository
public class SurveyDao {

    private JdbcTemplate jdbcTemplate;
    private List<GetSurveyRes> getSurveyRes;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }





    public List<GetSurveyRes> selectSurvey(){
        String selectSurveyQuery =
                "SELECT surveyIdx, surveyTitle, createdAt, deadlineAt, preferGender, preferAge,\n" +
                        "       surveyTime, hashtag, surveyCategoryIdx, surveyPointValue, totalParticipant, userIdx\n" +
                        "FROM Survey WHERE surveyStatus ='ACTIVE';";

        return this.jdbcTemplate.query(selectSurveyQuery,
                (rs,rowNum) -> new GetSurveyRes(
                        rs.getInt("surveyIdx"),
                        rs.getString("surveyTitle"),
                        rs.getString("createdAt"),
                        rs.getString("deadlineAt"),
                        //rs.getString("writerId"),
                        rs.getString("preferGender"),
                        rs.getInt("preferAge"),
                        rs.getInt("surveyTime"),
                        rs.getString("hashtag"),
                        rs.getInt("surveyCategoryIdx"),
                        rs.getInt("surveyPointValue"),
                        rs.getInt("totalParticipant"),
                        rs.getInt("userIdx")
                ));
    }

    public int insertSurvey(int userIdx, String surveyIntroduction, String surveyTitle,
                            int surveyCategoryIdx, String deadlineAt, String preferGender,
                            int preferAge, int surveyTime, String hashtag, int surveyPointValue, int couponIdx){
        String insertSurveyQuery = "INSERT INTO Survey(userIdx, surveyIntroduction, surveyTitle, surveyCategoryIdx,\n" +
                "        deadlineAt, preferGender, preferAge, surveyTime, hashtag, surveyPointValue, couponIdx)\n" +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);\n";
        Object [] insertSurveyParams = new Object[] {userIdx, surveyIntroduction, surveyTitle, surveyCategoryIdx,
        deadlineAt, preferGender, preferAge, surveyTime, hashtag, surveyPointValue, couponIdx};
        this.jdbcTemplate.update(insertSurveyQuery, insertSurveyParams);

        String lastInsertIdxQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdxQuery,int.class);

    }

    public int insertSurveyQuestion(int surveyIdx, PostSurveyQuestionReq postSurveyQuestionReq){
        String insertSurveyQuestionQuery = "INSERT INTO SurveyQuestion(surveyIdx, questionType, questionContent) VALUES (?,?,?);";
        Object [] insertSurveyQuestionParams = new Object[] {surveyIdx, postSurveyQuestionReq.getQuestionType(),
                postSurveyQuestionReq.getQuestionContent()};
        this.jdbcTemplate.update(insertSurveyQuestionQuery, insertSurveyQuestionParams);

        String lastInsertIdxQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdxQuery,int.class);

    }



    public int deleteSurvey(int surveyIdx){
        String deleteSurveyQuery = "UPDATE Survey SET surveyStatus='INACTIVE' WHERE surveyIdx=?";
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

    public List<String> selectSurveyCategoryList(){
        String selectSurveyCategoryListQuery = "select surveyCategoryTitle from SurveyCategory";
        List list = this.jdbcTemplate.queryForList(selectSurveyCategoryListQuery);
        List surveyCategoryList = new ArrayList();
        for (Iterator it = list.iterator(); it.hasNext();){
            Map map = (Map)it.next();
            String surveyCategory = (String)map.get("SurveyCategoryTitle");
            System.out.println(surveyCategory);
            surveyCategoryList.add(surveyCategory);
        }
        return surveyCategoryList;

    }

}


