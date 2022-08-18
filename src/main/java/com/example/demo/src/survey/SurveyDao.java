package com.example.demo.src.survey;

import com.example.demo.config.BaseException;
import com.example.demo.src.survey.model.GetSurveyQuestionOptionRes;
import com.example.demo.src.survey.model.GetSurveyQuestionRes;
import com.example.demo.src.survey.model.GetSurveyRes;
import com.example.demo.src.survey.model.PostSurveyQuestionOptionReq;
import com.example.demo.src.survey.model.PostSurveyQuestionReq;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.*;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;
import static com.example.demo.config.BaseResponseStatus.SURVEY_NOT_EXIST;

@Repository
public class SurveyDao {

    private JdbcTemplate jdbcTemplate;
    private List<GetSurveyRes> getSurveyRes;
    private List<GetSurveyQuestionOptionRes> getOptions;
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
        String insertSurveyQuestionQuery = "INSERT INTO SurveyQuestion(surveyIdx, questionType, questionContent) VALUES (?,?,?);";
        Object [] insertSurveyQuestionParams = new Object[] {surveyIdx, postSurveyQuestionReq.getQuestionType(),
        postSurveyQuestionReq.getQuestionContent()};
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
    public int checkSurveyExist(int surveyIdx){
        String checkSurveyExistQuery = "select exists(select surveyIdx from Survey where surveyIdx = ?)";
        int checkSurveyExistParams = surveyIdx;
        return this.jdbcTemplate.queryForObject(checkSurveyExistQuery,
                int.class,
                checkSurveyExistParams);

    }

    public Map<Integer,String> selectSurveyCategoryList(){
        String selectSurveyCategoryListQuery = "select surveyCategoryId, surveyCategoryTitle from SurveyCategory";
        HashMap<Integer,String> map = new HashMap<Integer,String>();
        this.jdbcTemplate.query(selectSurveyCategoryListQuery,
                (rs,rowNum) -> map.put(rs.getInt("surveyCategoryId"),rs.getString("surveyCategoryTitle")));
        return map;

    }

    /*
    get 객관식/주관식
     */
    public String getQuestionType(int questionIdx){
        String getQuestionTypeQuery ="SELECT questionType FROM SurveyQuestion WHERE questionIdx=?";
        int getQuestionTypeParam = questionIdx;
        return this.jdbcTemplate.queryForObject(getQuestionTypeQuery,String.class,getQuestionTypeParam);
    }
    /*
    주관식 답변 등록
     */
    public void insertSurveyEssayAnswer(int userIdx, int questionIdx, String questionAnswer){
        String insertSurveyEssayAnswerQuery = "INSERT INTO SurveyEssayAnswer(userIdx, questionIdx, essayAnswerContent) values (?, ?, ?)";
        Object [] insertSurveyEssayAnswerParams = new Object[] {userIdx, questionIdx,questionAnswer};
        this.jdbcTemplate.update(insertSurveyEssayAnswerQuery, insertSurveyEssayAnswerParams);
    }
    /*
    객관식 답변 등록
     */
    public void insertSurveyCheckboxAnswer(int userIdx,int questionIdx,int checkedOptionAnswerIdx){
        String insertSurveyCheckboxAnswerQuery = "INSERT INTO SurveyCheckedAnswer (userIdx, questionIdx, checkedOptionAnswerIdx) VALUES (?, ?, ?)";
        Object [] insertSurveyCheckboxAnswerParams = new Object[] {userIdx, questionIdx,checkedOptionAnswerIdx};
        this.jdbcTemplate.update(insertSurveyCheckboxAnswerQuery, insertSurveyCheckboxAnswerParams);
    }
    /*
    설문조사 조회
     */
    public GetSurveyRes selectSurveyOne(int surveyIdx){
        String selectSurveyQuery =
                "SELECT surveyIdx, surveyTitle, createdAt, deadlineAt, preferGender, preferAge,\n" +
                        "       surveyTime, hashtag, surveyCategoryIdx, surveyPointValue, totalParticipant, userIdx\n" +
                        "FROM Survey WHERE surveyIdx = ? ;";
        int selectSurveyParam = surveyIdx;
        return this.jdbcTemplate.queryForObject(selectSurveyQuery,
                (rs,rowNum) -> new GetSurveyRes(
                        rs.getInt("surveyIdx"),
                        rs.getString("surveyTitle"),
                        rs.getString("createdAt"),
                        rs.getString("deadlineAt"),
                        rs.getString("preferGender"),
                        rs.getInt("preferAge"),
                        rs.getInt("surveyTime"),
                        rs.getString("hashtag"),
                        rs.getInt("surveyCategoryIdx"),
                        rs.getInt("surveyPointValue"),
                        rs.getInt("totalParticipant"),
                        rs.getInt("userIdx")
                ),selectSurveyParam);
    }
    /*
    설문조사 문제들 조회
     */
    public List<GetSurveyQuestionRes>selectSurveyQuestions(int surveyIdx){
        String selectSurveyQuestionsQuery = "SELECT questionType, questionContent, questionIdx FROM SurveyQuestion WHERE surveyIdx = ?";
        String selectSurveyOptionsQuery = "SELECT optionIdx, optionContent FROM SurveyQuestionOption WHERE questionIdx = ?";
        int selectSurveyQuestionsParam = surveyIdx;
        return this.jdbcTemplate.query(selectSurveyQuestionsQuery,
                (rs,rowNum) -> new GetSurveyQuestionRes(
                        rs.getInt("questionIdx"),
                        rs.getString("questionType"),
                        rs.getString("questionContent"),
                        getOptions = this.jdbcTemplate.query(selectSurveyOptionsQuery,
                                (rk,rownum) -> new GetSurveyQuestionOptionRes(
                                        rk.getInt("optionIdx"),
                                        rk.getString("optionContent"))
                                ,rs.getInt("questionIdx"))),selectSurveyQuestionsParam);
    }
    /*
    작성자로 설문조사 조회
     */
    public List<GetSurveyRes> selectSurveyByUserIdx(int userIdx){
        String selectSurveyByUserIdxQuery = "SELECT surveyIdx, surveyTitle, createdAt, deadlineAt, preferGender, preferAge,\n" +
                "       surveyTime, hashtag, surveyCategoryIdx, surveyPointValue, totalParticipant, userIdx\n" +
                "FROM Survey WHERE userIdx = ?";
        int selectSurveyByUserIdxParam = userIdx;
        return this.jdbcTemplate.query(selectSurveyByUserIdxQuery,
                (rs, rowNum) -> new GetSurveyRes(
                        rs.getInt("surveyIdx"),
                        rs.getString("surveyTitle"),
                        rs.getString("createdAt"),
                        rs.getString("deadlineAt"),
                        rs.getString("preferGender"),
                        rs.getInt("preferAge"),
                        rs.getInt("surveyTime"),
                        rs.getString("hashtag"),
                        rs.getInt("surveyCategoryIdx"),
                        rs.getInt("surveyPointValue"),
                        rs.getInt("totalParticipant"),
                        rs.getInt("userIdx")
                ),selectSurveyByUserIdxParam);
    }
}


