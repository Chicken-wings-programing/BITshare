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

/*设置类,可用于更改设置*/
@Slf4j
@RestController
public class SettingController {
    // 更改头像
    @PostMapping("/setting/changeProfilePhoto")
    public CommonResult setting(@RequestParam("userId") String userId,
                                @RequestPart(value="profilePhoto")MultipartFile profilePhoto) throws IOException {
        log.info("post ：/register  -- 前端传入 userId: {}, profilePhoto: {}",
                userId, profilePhoto);
        DbController dbController = SpringUtil.getObject(DbController.class);

        CommonResult res = new CommonResult();
        if (dbController.setProfilePhoto(userId, profilePhoto)) {
            res.setCode(1);
            res.setInfo("更新头像成功！");
        } else {
            res.setCode(0);
            res.setInfo("更新头像失败,用户不存在！");
        }
        log.info("post ：/register  -- 后端响应 {}", res);
        return res;
    }
}
