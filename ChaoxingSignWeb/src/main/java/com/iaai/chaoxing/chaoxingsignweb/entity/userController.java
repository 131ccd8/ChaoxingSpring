package com.iaai.chaoxing.chaoxingsignweb.entity;

import com.iaai.chaoxing.chaoxingsignweb.controller.UserController;
import com.iaai.chaoxing.chaoxingsignweb.model.stringToMD5;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

/** JAVA中的一个实体类对应 Mysql一张表 */

@Data
@Entity
@Table(name = "userController")
public class userController {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String user;

    @Column(nullable = false)
    private String pass;

    @Column(length = 5000)
    private String cookie;

    @Column(length = 5000)
    private String userinfo;

    private LocalDateTime time;

    // 无参构造器
    public userController() {
        this.time = LocalDateTime.now();
    }
    // 全参构造器
    public userController(UserController userController) {
        this.user = userController.getUser();
        this.pass = userController.getPass();
        this.cookie = userController.getCookie();
        this.userinfo = userController.getUserinfo();
        this.time = LocalDateTime.now();
    }
}