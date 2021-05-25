package com.jichibiancheng.bitshare.controller;

import com.jichibiancheng.bitshare.api.CommonResult;
import com.jichibiancheng.bitshare.server.DbController;
import com.jichibiancheng.bitshare.utils.SpringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@RestController
public class RegisterController {

    /* 注册时，检测 id 是否重复 */
    @PostMapping("/register/userId")
    public CommonResult hasId(@RequestParam("userId") String userId) {

        log.info("post ：/register/userId  -- 前端传入 userId: {}", userId);

        DbController dbController = SpringUtil.getObject(DbController.class);

        CommonResult res = new CommonResult();
        if (dbController.hasUser(userId)) {
            res.setCode(0);
            res.setInfo("此用户名已存在");
        } else {
            res.setCode(1);
            res.setInfo("此用户名可注册");
        }

        log.info("post ：/register/userId  -- 后端响应 {}", res);
        return res;
    }

    @PostMapping("/register")
    public CommonResult register(
            @RequestParam("userId") String userId,
            @RequestParam("password") String password,
            @RequestParam("emailBox") String emailBox,
            //允许头像为空！
            @RequestPart(value = "profilePhoto", required = false) MultipartFile profilePhoto) throws IOException {

        log.info("post ：/register  -- 前端传入 userId: {}, password: {}, emailBox: {}, profilePhoto: {}",
                userId, password, emailBox, profilePhoto);

        DbController dbController = SpringUtil.getObject(DbController.class);

        CommonResult res = new CommonResult();
        if (dbController.insertUser(userId, password, emailBox, profilePhoto)) {
            res.setCode(1);
            res.setInfo("注册成功！");
        } else {
            res.setCode(0);
            res.setInfo("用户名已存在，注册失败！");
        }

        log.info("post ：/register  -- 后端响应 {}", res);
        return res;
    }
}
