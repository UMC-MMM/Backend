package com.example.demo.src.home;


import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.home.Models.GetHomeRes;
import com.example.demo.src.survey.SurveyService;
import com.example.demo.src.user.UserProvider;
import com.example.demo.src.user.UserService;
import com.example.demo.src.user.model.GetUserPointRes;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")
public class HomeController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final HomeProvider homeProvider;

    //@Autowired
    //private final HomeService homeService;

    @Autowired
    private final JwtService jwtService;

    public HomeController(HomeProvider homeProvider, JwtService jwtService) {
        this.homeProvider = homeProvider;
        this.jwtService = jwtService;
    }

    /**
     * 홈화면 조회 api
     */
    @ResponseBody
    @GetMapping("")
    public BaseResponse<GetHomeRes> getHome()  {
        try {
            GetHomeRes getHomeRes = homeProvider.getHome();
            return new BaseResponse<>(getHomeRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }

    }
}
