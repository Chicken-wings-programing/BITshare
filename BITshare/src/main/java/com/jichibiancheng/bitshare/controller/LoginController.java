package com.jichibiancheng.bitshare.controller;

import com.jichibiancheng.bitshare.api.CommonResult;
import com.jichibiancheng.bitshare.server.DbController;
import com.jichibiancheng.bitshare.utils.SpringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
public class LoginController {

//    在这取出组件会报错：SpringUtil中封装的context为空！
//    DbController dbController = SpringUtil.getObject(DbController.class);

    @PostMapping("/login")
    public CommonResult login(
            @RequestParam("userId") String userId,
            @RequestParam("password") String password) {

        log.info("post ：/login  -- 前端传入 userId: {}, password: {} ", userId, password);

        //在这取出组件就没问题
        DbController dbController = SpringUtil.getObject(DbController.class);

        DbController.LoginStates state = dbController.isUserValid(userId, password);
        CommonResult res = new CommonResult();

        switch (state) {
            case userNotExist:
                res.setCode(0);
                res.setInfo("用户名不存在！");
                break;
            case passWordNotMatch:
                res.setCode(1);
                res.setInfo("密码错误！");
                break;
            case LogInSuccess:
                res.setCode(2);
                res.setInfo("登陆成功！");
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + state);
        }
        log.info("post ：/login  -- 后端响应 {}", res);
        return res;
    }

}
