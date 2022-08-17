package com.example.demo.src.user;

import com.example.demo.config.BaseException;
import com.example.demo.src.user.model.*;
import com.example.demo.utils.JwtService;
import com.example.demo.utils.SHA256;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import static com.example.demo.config.BaseResponseStatus.*;


@Service
public class UserService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final UserDao userDao;
    private final UserProvider userProvider;
    private final JwtService jwtService;


    public UserService(UserDao userDao, UserProvider userProvider, JwtService jwtService) {
        this.userDao = userDao;
        this.userProvider = userProvider;
        this.jwtService = jwtService;

    }

    public PostUserRes createUser(PostUserReq postUserReq) throws BaseException {
        // 이메일 중복 확인
        if(userProvider.checkUserEmail(postUserReq.getUserEmail()) ==1){
            throw new BaseException(POST_USERS_EXISTS_EMAIL);
        }
        //아이디 중복 확인
        if(userProvider.checkUserId(postUserReq.getUserId()) ==1){
            throw new BaseException(USERS_EXISTS_USER_ID);
        }

        String pwd;
        try{
            //암호화
            pwd = new SHA256().encrypt(postUserReq.getUserPassword());
            postUserReq.setUserPassword(pwd);
        } catch (Exception ignored) {
            throw new BaseException(PASSWORD_ENCRYPTION_ERROR);
        }
        try{
            int userIdx = userDao.createUser(postUserReq);
            //jwt 발급.
            String jwt = jwtService.createJwt(userIdx);
            return new PostUserRes(jwt,userIdx);

        } catch (Exception exception) {
            System.out.println(exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public PostLoginRes login(PostLoginReq postLoginReq) throws BaseException {
        UserInfo userInfo = userDao.login(postLoginReq);
        String encryptPwd;
        try{
            encryptPwd = new SHA256().encrypt(postLoginReq.getPassword());
        } catch(Exception exception){
            throw new BaseException(PASSWORD_ENCRYPTION_ERROR);
        }
        if(userInfo.getUserPassword().equals(encryptPwd)){
            int userIdx = userInfo.getUserIdx();
            String jwt = jwtService.createJwt(userIdx);
            return new PostLoginRes(userIdx, jwt);
        }
        else{
            throw new BaseException(FAILED_TO_LOGIN);
        }
    }

    public void modifyUserProfile(int userIdx, PatchUserProfileReq patchUserProfileReq) throws BaseException{
        if(userProvider.checkUserExist(userIdx)==0){
            throw new BaseException(USERS_EMPTY_USER_ID);
        }
        try{
            int result = userDao.updateUserProfile(userIdx, patchUserProfileReq.getProfileImgUrl(),
                    patchUserProfileReq.getUserName(), patchUserProfileReq.getUserGender(),
                    patchUserProfileReq.getUserAge(),
                    patchUserProfileReq.getUserEmail());
            if(result == 0){
                throw new BaseException(MODIFY_FAIL_USERPROFILE);
            }
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }

    }


}
