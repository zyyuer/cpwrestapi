package com.tangue.cpw;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = "com.tangue.cpw.repository")
public class CpwApplication {
    public static void main(String[] args) {
        SpringApplication.run(CpwApplication.class, args);
    }
}
