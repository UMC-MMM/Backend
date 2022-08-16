package com.example.demo.src.survey;

import com.example.demo.src.survey.model.GetSurveyQuestionOptionRes;
import com.example.demo.src.survey.model.GetSurveyQuestionRes;
import com.example.demo.src.survey.model.GetSurveyRes;
import com.example.demo.src.survey.model.PostSurveyQuestionOptionReq;
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

        System.out.println(postSurveyQuestionOptionReq.getOptionContent());

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

    public String getQuestionType(int questionIdx){
        String getQuestionTypeQuery ="SELECT questionType FROM SurveyQuestion WHERE questionIdx=?";
        int getQuestionTypeParam = questionIdx;
        return this.jdbcTemplate.queryForObject(getQuestionTypeQuery,String.class,getQuestionTypeParam);
    }

    public void insertSurveyEssayAnswer(int userIdx, int questionIdx, String questionAnswer){
        String insertSurveyEssayAnswerQuery = "INSERT INTO SurveyEssayAnswer(userIdx, questionIdx, essayAnswerContent) values (?, ?, ?)";
        Object [] insertSurveyEssayAnswerParams = new Object[] {userIdx, questionIdx,questionAnswer};
        this.jdbcTemplate.update(insertSurveyEssayAnswerQuery, insertSurveyEssayAnswerParams);
    }
    public void insertSurveyCheckboxAnswer(int userIdx,int questionIdx,int checkedOptionAnswerIdx){
        String insertSurveyCheckboxAnswerQuery = "INSERT INTO SurveyCheckedAnswer (userIdx, questionIdx, checkedOptionAnswerIdx) VALUES (?, ?, ?)";
        Object [] insertSurveyCheckboxAnswerParams = new Object[] {userIdx, questionIdx,checkedOptionAnswerIdx};
        this.jdbcTemplate.update(insertSurveyCheckboxAnswerQuery, insertSurveyCheckboxAnswerParams);
    }
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
    public List<GetSurveyQuestionRes>selectSurveyQuestions(int surveyIdx){
        String selectSurveyQuestionsQuery = "SELECT questionType, questionContent, questionIdx FROM SurveyQuestion WHERE surveyIdx = ?";
        String selectSurveyOptionsQuery = "SELECT optionIdx, optionContent FROM SurveyQuestionOption WHERE questionIdx = ?";
        int selectSurveyQuestionsParam = surveyIdx;
        return this.jdbcTemplate.query(selectSurveyQuestionsQuery,
                (rs,rowNum) -> new GetSurveyQuestionRes(
                        rs.getString("questionType"),
                        rs.getString("questionContent"),
                        getOptions = this.jdbcTemplate.query(selectSurveyOptionsQuery,
                                (rk,rownum) -> new GetSurveyQuestionOptionRes(
                                        rk.getInt("optionIdx"),
                                        rk.getString("optionContent"))
                                ,rs.getInt("questionIdx"))),selectSurveyQuestionsParam);
    }
}


