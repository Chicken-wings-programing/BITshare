package com.jichibiancheng.bitshare.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class MailController {
    private JavaMailSender javaMailSender;

    @Autowired
    public MailController(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    //向 mailTo 发送邮件，验证码为 code
    public Boolean sendIdentificationCode(String id, String mailTo, String code) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();

        String mailFrom = "89657899@qq.com";
        simpleMailMessage.setFrom(mailFrom);   //设置发送邮箱
        simpleMailMessage.setTo(mailTo);  //设置接受邮箱

        simpleMailMessage.setSubject("BITshare-app: identification code.");  //设置主题
        simpleMailMessage.setText("Hi! " + id + "~\nYour identification code is:  " + code);  //设置内容:这里嵌入静态资源以获得邮件界面

        //发送
        try {
            javaMailSender.send(simpleMailMessage);
            return true;
        } catch (MailException e) {
            return false;
        }
    }
}
