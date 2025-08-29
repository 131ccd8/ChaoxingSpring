package com.iaai.chaoxing.chaoxingsignweb.service;

import com.iaai.chaoxing.chaoxingsignweb.controller.UserController;
import com.iaai.chaoxing.chaoxingsignweb.entity.userController;
import com.iaai.chaoxing.chaoxingsignweb.model.stringToMD5;
import com.iaai.chaoxing.chaoxingsignweb.repository.userControllerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.json.JSONObject;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Service
public class userControllerService {

    private final userControllerRepository userRepository;

    // 构造器注入
    public userControllerService(userControllerRepository userRepository) { this.userRepository = userRepository; }

    @Transactional
    public String login(UserController userController) {
        if (userRepository.existsByUser(userController.getUser()) && isOlderThan30Days(userRepository.findTimesByUser(userController.getUser()))) {
            return "当前用户已登录";
        }

        vpnHttp vpnHttp = new vpnHttp();
        String response = vpnHttp.GET("https://passport2-api.chaoxing.com/v11/loginregister?code="+userController.getPass()+"&cx_xxt_passport=json&uname="+userController.getUser()+"&loginType=1&roleSelect=true",true);

        userController.setPass(new stringToMD5(userController.getPass()).input);
        //登录完成后把密码进行MD5加密，禁止明文传输

        try {
            JSONObject json = new JSONObject(response);
            String mes = json.getString("mes");
            if (mes.equals("验证通过")){
                String cookie = vpnHttp.mergeCookiesFromHeaders();
                String userInfo = vpnHttp.GET(json.getString("url"),true);
                userController.setUserinfo(userInfo);
                //获取用户信息
                userController.setCookie(vpnHttp.mergeCookiesFromHeaders());
                userController newUser = new userController(userController);
                userRepository.save(newUser);
                return "登录成功";
            }else{
                return mes;
            }
        } catch (Exception e) {
            return "账户登录异常";
        }
    }

    @Transactional
    public String logout(UserController userController) {
        if (!userRepository.existsByUser(userController.getUser())) {
            return "您还没有登录";
        } else if (!userRepository.findPassByUser(userController.getUser()).equals(userController.getPass())) {
            return "密码不正确";
        }
        userRepository.deleteByUsername(userController.getUser());
        return  "已退出登录";
    }

    //判断cookie登录时间是否大于30天
    public boolean isOlderThan30Days(long timestamp) {
        Date date = new Date(timestamp);
        Date now = new Date();
        long diffInMillis = now.getTime() - date.getTime();
        long days = TimeUnit.MILLISECONDS.toDays(diffInMillis);
        return days > 30;
    }
}
