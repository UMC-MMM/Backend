package com.example.demo.src.survey;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.survey.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.*;

@Repository
public class SurveyDao {

    private JdbcTemplate jdbcTemplate;
    private List<GetSurveyRes> getSurveyRes;
    private List<GetSurveyQuestionOptionRes> getOptions;
    private int count;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }


    /*
    설문조사 목록
     */
    public List<GetSurveyRes> selectSurvey() {
        String selectSurveyQuery =
                "SELECT s.surveyIdx, s.surveyTitle, s.createdAt, s.deadlineAt, s.preferGender, s.preferAge,\n" +
                        "       s.surveyTime, s.hashtag, s.surveyCategoryIdx, s.surveyPointValue, s.totalParticipant, s.userIdx, u.userName\n" +
                        "FROM User as u left join Survey as s on u.userIdx = s.userIdx \n" +
                        "WHERE s.surveyStatus ='ACTIVE'";

        return this.jdbcTemplate.query(selectSurveyQuery,
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
                        rs.getInt("userIdx"),
                        rs.getString("userName")
                ));
    }

    /*
    설문조사 등록
     */
    public int insertSurvey(int userIdx, String surveyIntroduction, String surveyTitle,
                            int surveyCategoryIdx, String deadlineAt, String preferGender,
                            int preferAge, int surveyTime,
                            String hashtag,
                            int surveyPointValue, int couponIdx) {
        String insertSurveyQuery = "INSERT INTO Survey(userIdx, surveyIntroduction, surveyTitle, surveyCategoryIdx,\n" +
                "        deadlineAt, preferGender, preferAge, hashtag, couponIdx)\n" +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);\n";
        Object[] insertSurveyParams = new Object[]{userIdx, surveyIntroduction, surveyTitle, surveyCategoryIdx,
                deadlineAt, preferGender, preferAge,
                hashtag, couponIdx};
        this.jdbcTemplate.update(insertSurveyQuery, insertSurveyParams);

        String lastInsertIdxQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdxQuery, int.class);

    }

    /*
    설문조사 참여자 수 증가
     */
    public int countParticipant(int surveyIdx) {
        String countParticipantQuery = "UPDATE Survey SET totalParticipant=totalParticipant + 1 WHERE surveyIdx=?";
        Object[] countParticipantParams = new Object[]{surveyIdx};
        return this.jdbcTemplate.update(countParticipantQuery, countParticipantParams);

    }

    /*
    get 체크박스 수
     */
    public int getCheckboxCnt(int surveyIdx) {
        String getCheckboxCntQuery = "SELECT sum(case when questionType='Checkbox' AND surveyIdx = ? then 1 END)\n" +
                "    as CheckboxCnt FROM SurveyQuestion";
        int getCheckboxCntParam = surveyIdx;
        return this.jdbcTemplate.queryForObject(getCheckboxCntQuery, int.class, getCheckboxCntParam);
    }

    /*
    get 서술형 수
     */
    public int getEssayCnt(int surveyIdx) {
        String getEssayCntQuery = "SELECT sum(case when questionType='ESSAY' AND surveyIdx = ? then 1 END)\n" +
                "    as EssayCnt FROM SurveyQuestion";
        int getEssayCntParam = surveyIdx;
        return this.jdbcTemplate.queryForObject(getEssayCntQuery, int.class, getEssayCntParam);
    }

    /*
    설문조사 소요시간 설정
     */
    public int setSurveyTime(int surveyTime, int surveyIdx) {
        String setSurveyTimeQuery = "UPDATE Survey SET surveyTime = ? WHERE surveyIdx = ?;";
        Object[] setSurveyTimeParams = new Object[]{surveyTime, surveyIdx};
        return this.jdbcTemplate.update(setSurveyTimeQuery, setSurveyTimeParams);

    }

    /*
    설문조사 질문
     */
    public int insertSurveyQuestion(int surveyIdx, PostSurveyQuestionReq postSurveyQuestionReq) {
        String insertSurveyQuestionQuery = "INSERT INTO SurveyQuestion(surveyIdx, questionType, questionContent) VALUES (?,?,?);";
        Object[] insertSurveyQuestionParams = new Object[]{surveyIdx, postSurveyQuestionReq.getQuestionType(),
                postSurveyQuestionReq.getQuestionContent()};
        this.jdbcTemplate.update(insertSurveyQuestionQuery, insertSurveyQuestionParams);


        String lastInsertIdxQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdxQuery, int.class);

    }

    /*
    설문조사 질문 옵션(객관식, 체크박스)
    */
    public int insertSurveyQuestionOption(int questionIdx, PostSurveyQuestionOptionReq postSurveyQuestionOptionReq) {
        String insertSurveyQuestionOptionQuery = "INSERT INTO SurveyQuestionOption(questionIdx, OptionContent) VALUES (?,?);";
        Object[] insertSurveyQuestionOptionParams = new Object[]{questionIdx, postSurveyQuestionOptionReq.getOptionContent()};

        this.jdbcTemplate.update(insertSurveyQuestionOptionQuery, insertSurveyQuestionOptionParams);

        String lastInsertIdxQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdxQuery, int.class);

    }




    /*
    설문조사 삭제
     */
    public int deleteSurvey(int surveyIdx) {
        String deleteSurveyQuery = "UPDATE Survey SET surveyStatus='DELETE' WHERE surveyIdx=?";
        Object[] deleteSurveyParams = new Object[]{surveyIdx};

        return this.jdbcTemplate.update(deleteSurveyQuery, deleteSurveyParams);

    }

    public int checkUserExist(int userIdx) {
        String checkUserExistQuery = "select exists(select userIdx from User where userIdx = ?)";
        int checkUserExistParams = userIdx;
        return this.jdbcTemplate.queryForObject(checkUserExistQuery,
                int.class,
                checkUserExistParams);

    }

    public int checkSurveyExist(int surveyIdx) {
        String checkSurveyExistQuery = "select exists(select surveyIdx from Survey where surveyIdx = ?)";
        int checkSurveyExistParams = surveyIdx;
        return this.jdbcTemplate.queryForObject(checkSurveyExistQuery,
                int.class,
                checkSurveyExistParams);

    }

    public int checkSurveyIsValid(int surveyIdx) {
        String checkSurveyIsValidQuery = "select surveyStatus surveyIdx from Survey where surveyIdx = ?";
        int checkSurveyIsValidParam = surveyIdx;
        String surveyStat = jdbcTemplate.queryForObject(checkSurveyIsValidQuery,
                String.class,
                checkSurveyIsValidParam);
        if(surveyStat.equals("ACTIVE")){
            return 1;
        } else { //surveyStat == "INACTIVE"
            return 0;
        }
    }

    /*
    내 설문조사인지
     */
    public boolean checkMySurvey(int userIdx, int surveyIdx) {
        String checkMySurveyQuery = "SELECT surveyIdx FROM Survey WHERE userIdx = ?";
        int checkMySurveyParam = userIdx;
        List<Integer> mySurveyIdxList = this.jdbcTemplate.queryForList(checkMySurveyQuery, int.class,
                checkMySurveyParam);
        for (int i = 0; i < mySurveyIdxList.size(); i++) {
            if (mySurveyIdxList.get(i) == surveyIdx) {
                return true;
            }
        }
        return false;
    }

    public Map<Integer, String> selectSurveyCategoryList() {
        String selectSurveyCategoryListQuery = "select surveyCategoryId, surveyCategoryTitle from SurveyCategory";
        HashMap<Integer, String> map = new HashMap<Integer, String>();
        this.jdbcTemplate.query(selectSurveyCategoryListQuery,
                (rs, rowNum) -> map.put(rs.getInt("surveyCategoryId"), rs.getString("surveyCategoryTitle")));
        return map;

    }

    /*
    get 객관식/주관식
     */
    public String getQuestionType(int questionIdx) {
        String getQuestionTypeQuery = "SELECT questionType FROM SurveyQuestion WHERE questionIdx=?";
        int getQuestionTypeParam = questionIdx;
        return this.jdbcTemplate.queryForObject(getQuestionTypeQuery, String.class, getQuestionTypeParam);
    }

    /*
    주관식 답변 등록
     */
    public void insertSurveyEssayAnswer(int userIdx, int questionIdx, String questionAnswer) {
        String insertSurveyEssayAnswerQuery = "INSERT INTO SurveyEssayAnswer(userIdx, questionIdx, essayAnswerContent) values (?, ?, ?)";
        Object[] insertSurveyEssayAnswerParams = new Object[]{userIdx, questionIdx, questionAnswer};
        this.jdbcTemplate.update(insertSurveyEssayAnswerQuery, insertSurveyEssayAnswerParams);
    }

    /*
    객관식 답변 등록
     */
    public void insertSurveyCheckboxAnswer(int userIdx, int questionIdx, int checkedOptionAnswerIdx) {
        String insertSurveyCheckboxAnswerQuery = "INSERT INTO SurveyCheckedAnswer (userIdx, questionIdx, checkedOptionAnswerIdx) VALUES (?, ?, ?)";
        Object[] insertSurveyCheckboxAnswerParams = new Object[]{userIdx, questionIdx, checkedOptionAnswerIdx};
        this.jdbcTemplate.update(insertSurveyCheckboxAnswerQuery, insertSurveyCheckboxAnswerParams);
    }

    /*
    설문조사 조회
     */
    public GetSurveyRes selectSurveyOne(int surveyIdx) {
        String selectSurveyQuery =
                "SELECT s.surveyIdx, s.surveyTitle, s.createdAt, s.deadlineAt, s.preferGender, s.preferAge,\n" +
                        "       s.surveyTime, s.hashtag, s.surveyCategoryIdx, s.surveyPointValue, s.totalParticipant, s.userIdx, u.userName\n" +
                        "FROM User as u left join Survey as s on u.userIdx = s.userIdx \n" +
                        "WHERE s.surveyStatus ='ACTIVE' and s.surveyIdx = ?;";
        int selectSurveyParam = surveyIdx;
        return this.jdbcTemplate.queryForObject(selectSurveyQuery,
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
                        rs.getInt("userIdx"),
                        rs.getString("userName")
                ), selectSurveyParam);
    }

    /*
    설문조사 문제들 조회
     */
    public List<GetSurveyQuestionRes> selectSurveyQuestions(int surveyIdx) {
        String selectSurveyQuestionsQuery = "SELECT questionType, questionContent, questionIdx FROM SurveyQuestion WHERE surveyIdx = ?";
        String selectSurveyOptionsQuery = "SELECT optionIdx, optionContent FROM SurveyQuestionOption WHERE questionIdx = ?";
        int selectSurveyQuestionsParam = surveyIdx;
        return this.jdbcTemplate.query(selectSurveyQuestionsQuery,
                (rs, rowNum) -> new GetSurveyQuestionRes(
                        rs.getInt("questionIdx"),
                        rs.getString("questionType"),
                        rs.getString("questionContent"),
                        getOptions = this.jdbcTemplate.query(selectSurveyOptionsQuery,
                                (rk, rownum) -> new GetSurveyQuestionOptionRes(
                                        rk.getInt("optionIdx"),
                                        rk.getString("optionContent"))
                                , rs.getInt("questionIdx"))), selectSurveyQuestionsParam);
    }

    /*
    작성자로 설문조사 조회
     */
    public List<GetSurveyRes> selectSurveyByUserIdx(int userIdx) {
        String selectSurveyByUserIdxQuery = "SELECT s.surveyIdx, s.surveyTitle, s.createdAt, s.deadlineAt, s.preferGender, s.preferAge,\n" +
                "       s.surveyTime, s.hashtag, s.surveyCategoryIdx, s.surveyPointValue, s.totalParticipant, s.userIdx, u.userName\n" +
                "FROM User as u left join Survey as s on u.userIdx = s.userIdx \n" +
                "WHERE s.surveyStatus ='ACTIVE' and s.userIdx = ?";
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
                        rs.getInt("userIdx"),
                        rs.getString("userName")
                ), selectSurveyByUserIdxParam);
    }

    public List<AnswerResult> selectAnswerResult(int questionIdx) {
        String questionType = getQuestionType(questionIdx);
        if (questionType.equals("Essay")) {
            try {
                String selectEssayAnswerQuery = "SELECT essayAnswerContent From SurveyEssayAnswer Where questionIdx =?";
                int selectEssayAnswerParam = questionIdx;
                return this.jdbcTemplate.query(selectEssayAnswerQuery,
                        (rs, rowNum) -> new AnswerResultEssay(
                                rs.getString("essayAnswerContent")
                        ), selectEssayAnswerParam);
            } catch ( EmptyResultDataAccessException e){
                return null;
            }
        } else { //if (questionType == "Checkbox")
            try{String selectCheckboxAnswerQuery = "SELECT optionContent , optionIdx FROM SurveyQuestionOption WHERE questionIdx = ?";
                String selectCountOptionCheckQuery = "SELECT COUNT(*) FROM SurveyCheckedAnswer where checkedOptionAnswerIdx =?";
                int selectCheckboxAnswerParam = questionIdx;
                return this.jdbcTemplate.query(selectCheckboxAnswerQuery,
                        (rs, rowNum) -> new AnswerResultCheckBox(
                                rs.getString("optionContent"),
                                count = this.jdbcTemplate.queryForObject(selectCountOptionCheckQuery, int.class, rs.getInt("optionIdx"))
                        ), selectCheckboxAnswerParam);
            }catch (EmptyResultDataAccessException e){
                return null;
            }
        }
    }

    public List<QuestionResult> selectQuestionResult(int surveyIdx) {

        String selectQuestionResultQuery = "SELECT questionContent, questionIdx FROM SurveyQuestion where surveyIdx =?";
        int selectQuestionResultParam = surveyIdx;
        return this.jdbcTemplate.query(selectQuestionResultQuery,
                (rs,rowNum) -> new QuestionResult(
                        rs.getString("questionContent"),
                        selectAnswerResult(rs.getInt("questionIdx"))
                ),selectQuestionResultParam);

    }

}


