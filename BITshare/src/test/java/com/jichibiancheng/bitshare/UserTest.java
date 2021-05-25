package com.jichibiancheng.bitshare;

import com.jichibiancheng.bitshare.server.DbController;
import com.jichibiancheng.bitshare.utils.SpringUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

@Slf4j
@SpringBootTest
public class UserTest {

    @Test
    void addUser() throws IOException {
        DbController dbController = SpringUtil.getObject(DbController.class);

        /* 注册功能测试 */
        dbController.insertUser("ha", "xi", "1286746591@qq.com", null);

    }
    @Test
    void loginRegister() throws IOException {
        DbController dbController = SpringUtil.getObject(DbController.class);

        /* 注册功能测试 */
        dbController.insertUser("nihaoya", "abcdefg", "1120182917@bit.edu.cn", null);
        dbController.insertUser("little cat3", "catdogcat", "540614105@qq.com", null);

        /* 登录功能测试 */
        System.out.println(dbController.isUserValid("nihaoya", "abcdefg"));
        System.out.println(dbController.isUserValid("little cat", "ad"));

        if(dbController.updateUserPassword("little cat", "")){
            log.info("update password success");
        }
    }

    @Test
    void findPassword() throws IOException {
        DbController dbController = SpringUtil.getObject(DbController.class);
        /* 找回密码测试 */
        // 发送邮件
        if (dbController.sendEmail("little cat"))
            log.info("email is sent");
        else
            log.info("email is not sent");

    }
}
