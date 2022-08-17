package com.example.demo.src.user;

import com.example.demo.config.BaseException;
import com.example.demo.src.user.model.GetPointRes;
import com.example.demo.src.user.model.GetUserPointRes;
import com.example.demo.src.user.model.GetUserProfileRes;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;

@Service
public class UserProvider {
    private final UserDao userDao;
    private final JwtService jwtService;


    final Logger logger = LoggerFactory.getLogger(this.getClass());

    public UserProvider(UserDao userDao, JwtService jwtService) {
        this.userDao = userDao;
        this.jwtService = jwtService;
    }

    public int checkUserEmail(String email) throws BaseException {
        try{
            return userDao.checkUserEmail(email);
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int checkUserId(String id) throws BaseException {
        try{
            return userDao.checkUserId(id);
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int checkUserExist(int userIdx) throws BaseException{
        //validation check
        try{
            return userDao.checkUserExist(userIdx);
        } catch (Exception exception){
            throw new BaseException(USERS_EMPTY_USER_ID);
        }
    }

    public GetUserPointRes getUserPoint(int userIdx) throws BaseException{

        if(checkUserExist(userIdx)==0){
            throw new BaseException(USERS_EMPTY_USER_ID);
        }
        try{
            List<GetPointRes> plusPointHistory = userDao.selectUserPointPlus(userIdx);
            List<GetPointRes> minusPointHistory = userDao.selectUserPointMinus(userIdx);
            int PointPlusSum = userDao.getPointPlusSum(userIdx);
            int PointMinusSum = userDao.getPointMinusSum(userIdx);
            int totalPoint = PointPlusSum - PointMinusSum;
            if(totalPoint<0){
                throw new BaseException(TOTAL_POINT_MINUS);
            }
            GetUserPointRes getUserPointRes = new GetUserPointRes(userIdx, totalPoint,plusPointHistory,minusPointHistory);
            return getUserPointRes;
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public GetUserProfileRes getUserProfile(int userIdx) throws BaseException{

        if(checkUserExist(userIdx)==0){
            throw new BaseException(USERS_EMPTY_USER_ID);
        }
        try{
            GetUserProfileRes getUserProfile = userDao.getUserProfile(userIdx);
            return getUserProfile;
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
