package com.iaai.chaoxing.chaoxingsignweb.repository;

import com.iaai.chaoxing.chaoxingsignweb.controller.UserController;
import com.iaai.chaoxing.chaoxingsignweb.service.ApiResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface WebAPI {
    /** 用户登录接口 */
    @PostMapping("/login")
    ApiResponse<String> login(@RequestBody UserController userController);
    /** 退出登录接口 */
    @PostMapping("/logout")
    ApiResponse<String> logout(@RequestBody UserController userController);
}
