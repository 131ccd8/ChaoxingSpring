package com.iaai.chaoxing.chaoxingsignweb.controller;

import com.iaai.chaoxing.chaoxingsignweb.model.stringToMD5;
import com.iaai.chaoxing.chaoxingsignweb.repository.WebAPI;
import com.iaai.chaoxing.chaoxingsignweb.service.ApiResponse;
import com.iaai.chaoxing.chaoxingsignweb.service.userControllerService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController implements WebAPI {

    private final userControllerService userService;

    // 构造器注入
    public AuthController(userControllerService userService) { this.userService = userService; }

    @Override
    @PostMapping("/login")
    public ApiResponse<String> login(UserController userController) {
        try {
            return ApiResponse.success(userService.login(userController));
        } catch (RuntimeException e) {
            return ApiResponse.error(500,"系统错误");
        }
    }

    @Override
    @PostMapping("/logout")
    public ApiResponse<String> logout(UserController userController) {
        try {
            userController.setPass(new stringToMD5(userController.getPass()).input);
            //把密码进行MD5加密，禁止明文传输
            return ApiResponse.success(userService.logout(userController));
        } catch (RuntimeException e) {
            return ApiResponse.error(500,"系统错误");
        }
    }
}
