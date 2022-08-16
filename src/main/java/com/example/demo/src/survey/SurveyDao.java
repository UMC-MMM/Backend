package com.example.demo.src.survey;

import com.example.demo.src.survey.model.GetSurveyRes;
import com.example.demo.src.survey.model.PostSurveyQuestionOptionReq;
import com.example.demo.src.survey.model.PostSurveyQuestionReq;
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




/*
설문조사 목록
 */
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

    /*
    설문조사 등록
     */
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

    /*
    설문조사 질문
     */
    public int insertSurveyQuestion(int surveyIdx, PostSurveyQuestionReq postSurveyQuestionReq){
        String insertSurveyQuestionQuery = "INSERT INTO SurveyQuestion(surveyIdx, questionType) VALUES (?,?);";
        Object [] insertSurveyQuestionParams = new Object[] {surveyIdx, postSurveyQuestionReq.getQuestionType()};
        this.jdbcTemplate.update(insertSurveyQuestionQuery, insertSurveyQuestionParams);

        String lastInsertIdxQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdxQuery,int.class);

    }
    /*
    설문조사 질문 옵션(객관식, 체크박스)
    */
    public int insertSurveyQuestionOption(int questionIdx, PostSurveyQuestionOptionReq postSurveyQuestionOptionReq){
        String insertSurveyQuestionOptionQuery = "INSERT INTO SurveyQuestionOption(questionIdx, OptionContent) VALUES (?,?);";
        Object [] insertSurveyQuestionOptionParams = new Object[] {questionIdx, postSurveyQuestionOptionReq.getOptionContent()};
        System.out.println(postSurveyQuestionOptionReq.getOptionContent());
        this.jdbcTemplate.update(insertSurveyQuestionOptionQuery, insertSurveyQuestionOptionParams);

        String lastInsertIdxQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdxQuery,int.class);

    }


/*
설문조사 삭제
 */
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

}


