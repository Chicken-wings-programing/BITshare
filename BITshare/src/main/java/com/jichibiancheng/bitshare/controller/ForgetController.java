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
public class ForgetController {

    @PostMapping("/forget/mailbox")
    public CommonResult postEmail(@RequestParam("userId") String userId) {

        DbController dbController = SpringUtil.getObject(DbController.class);
        CommonResult res = new CommonResult();

        log.info("post ：/forget/mailbox  -- 前端传入 userId: {}", userId);

        if (dbController.sendEmail(userId)) {
            res.setCode(1);
            res.setInfo("发送成功");
        } else {
            res.setCode(0);
            res.setInfo("发送失败，用户不存在或非法邮箱");
        }

        log.info("post ：/forget/mailbox  -- 后端响应 {}", res);
        return res;
    }

    @PostMapping("/forget/check")
    public CommonResult check(
            @RequestParam("userId") String userId,
            @RequestParam("checkCode") String checkCode) {

        DbController dbController = SpringUtil.getObject(DbController.class);
        CommonResult res = new CommonResult();

        log.info("post ：/forget/check  -- 前端传入 userId: {}, checkCode: {}", userId, checkCode);

        if (dbController.checkCode(userId, checkCode)) {
            res.setCode(1);
            res.setInfo("验证通过");
        } else {
            res.setCode(0);
            res.setInfo("验证码不匹配");
        }

        log.info("post ：/forget/check  -- 后端响应 {}", res);
        return res;
    }

    @PostMapping("forget/reset")
    public CommonResult reset(@RequestParam("userId") String userId,
                              @RequestParam("newPassword") String newPassword) {

        DbController dbController = SpringUtil.getObject(DbController.class);
        CommonResult res = new CommonResult();

        log.info("post ：forget/reset  -- 前端传入 userId: {}, nwePassword: {}", userId, newPassword);

        if (dbController.updateUserPassword(userId, newPassword)) {
            res.setCode(1);
            res.setInfo("重设密码成功");
        } else {
            res.setCode(0);
            res.setInfo("重设失败，密码和之前一样 或 用户ID不存在");
        }

        log.info("post ：forget/reset  -- 后端响应 {}", res);
        return res;
    }

}
