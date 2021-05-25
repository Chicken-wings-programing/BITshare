package com.jichibiancheng.bitshare;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.unit.DataSize;

import javax.servlet.MultipartConfigElement;

@Configuration
@SpringBootApplication
public class BitshareApplication {

    public static void main(String[] args) {
        SpringApplication.run(BitshareApplication.class, args);
    }

}
