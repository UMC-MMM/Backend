package com.example.demo.src.user;

import com.example.demo.src.user.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class UserDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }


    public int createUser(PostUserReq postUserReq){
        //userID, userName,userEmail, userPassword
        String createUserQuery = "insert into User(userId, userPassword, userName, userEmail) VALUES (?,?,?,?)";
        Object[] createUserParams = new Object[]{postUserReq.getUserId(), postUserReq.getUserPassword(),
                postUserReq.getUserName(), postUserReq.getUserEmail()};
        this.jdbcTemplate.update(createUserQuery, createUserParams);
        String lastInsertIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdQuery,int.class);
    }


    public int checkUserId(String userId){
        String checkUserQuery = "select exists(select userId from User where userId = ?)";
        String checkUserParams = userId;
        return this.jdbcTemplate.queryForObject(checkUserQuery, int.class, checkUserParams);
    }


    public int checkUserEmail(String email){
        String checkEmailQuery = "select exists(select userEmail from User where userEmail = ?)";
        String checkEmailParams = email;
        return this.jdbcTemplate.queryForObject(checkEmailQuery,
                int.class,
                checkEmailParams);

    }

    public int checkUserExist(int userIdx){
        //validation check

        String checkUserExistQuery = "select exists(select userIdx from User where userIdx = ?)";
        int checkUserExistParams = userIdx;
        return this.jdbcTemplate.queryForObject(checkUserExistQuery,
                int.class,
                checkUserExistParams);

    }

    public int checkUserName(String name){
        String checkNameQuery = "SELECT exists(SELECT userName FROM User WHERE userName = ?)";
        String checkNameParams = name;
        return this.jdbcTemplate.queryForObject(checkNameQuery,
                int.class,
                checkNameParams);

    }


    public UserInfo login(PostLoginReq postLoginReq){
        String getPwdQuery = "select userIdx, userId, userName, userEmail, userPassword from User where userId = ?";
        String getPwdParams = postLoginReq.getId();

        return this.jdbcTemplate.queryForObject(getPwdQuery,
                (rs, rowNum)-> new UserInfo(
                        rs.getInt("userIdx"),
                        rs.getString("userId"),
                        rs.getString("userPassword"),
                        rs.getString("userName"),
                        rs.getString("userEmail")
                ),
                getPwdParams);
    }
    public List<GetPointRes> selectUserPointPlus(int userIdx){
        String selectUserPointPlusQuery =
                "select pointIdx,pointValue,createdAt,pointContent from PointPlus where userIdx=? \n" +
                "order by PointPlus.createdAt desc";
        int selectUserPointPlusParam = userIdx;
        return this.jdbcTemplate.query(selectUserPointPlusQuery,
                (rs,rowNum) -> new GetPointRes(
                        rs.getInt("pointIdx"),
                        rs.getInt("pointValue"),
                        rs.getString("createdAt"),
                        rs.getString("pointContent")
                ),selectUserPointPlusParam);
    }
    public List<GetPointRes> selectUserPointMinus(int userIdx){
        String selectUserPointMinusQuery =
                "select pointIdx,pointValue,usedAt,pointContent from PointMinus where userIdx=? \n" +
                "order by PointMinus.usedAt desc";
        int selectUserPointMinusParam = userIdx;
        return this.jdbcTemplate.query(selectUserPointMinusQuery,
                (rs,rowNum) -> new GetPointRes(
                        rs.getInt("pointIdx"),
                        rs.getInt("pointValue"),
                        rs.getString("usedAt"),
                        rs.getString("pointContent")
                ),selectUserPointMinusParam);
    }
    public int getPointPlusSum(int userIdx){
        String getPointPlusSumQuery = "select SUM(pointValue) from PointPlus where userIdx=?";
        int getPointPlusSumParam = userIdx;
        return this.jdbcTemplate.queryForObject(getPointPlusSumQuery,int.class,getPointPlusSumParam);
    }

    public int getPointMinusSum(int userIdx){
        String getPointMinusSumQuery = "select SUM(pointValue) from PointMinus where userIdx=?";
        int getPointMinusSumParam = userIdx;
        return this.jdbcTemplate.queryForObject(getPointMinusSumQuery,int.class,getPointMinusSumParam);
    }

    /*
    유저 프로필 조회
     */
    public GetUserProfileRes getUserProfile(int userIdx){
        String getUserProfileQuery = "SELECT userIdx,profileImgUrl, userName, userGender, userAge, userEmail FROM User WHERE userIdx=?";
        int getUserProfileParams = userIdx;
        return this.jdbcTemplate.queryForObject(getUserProfileQuery,
                (rs, rowNum) -> new GetUserProfileRes(
                        rs.getInt("userIdx"),
                        rs.getString("profileImgUrl"),
                        rs.getString("userName"),
                        rs.getString("userGender"),
                        rs.getInt("userAge"),
                        rs.getString("userEmail")),
                getUserProfileParams);
    }

    public int updateUserProfile(int userIdx, String profileImgUrl, String userName,
                                 String userGender, int userAge, String userEmail){
        String updateUserProfileQuery = "UPDATE User SET profileImgUrl=?, userName=?, userGender=?, userAge=?, userEmail=? WHERE userIdx=?";
        Object [] updateUserProfileParams = new Object[] {
                profileImgUrl, userName, userGender, userAge, userEmail, userIdx};
        return this.jdbcTemplate.update(updateUserProfileQuery, updateUserProfileParams);

    }


}
