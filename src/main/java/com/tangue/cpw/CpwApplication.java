package com.tangue.cpw;

import org.mybatis.spring.annotation.MapperScan;
        import org.springframework.boot.SpringApplication;
        import org.springframework.boot.autoconfigure.SpringBootApplication;
        import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@MapperScan(basePackages = "com.tangue.cpw.repository")
@EnableTransactionManagement
public class CpwApplication {
    public static void main(String[] args) {
        SpringApplication.run(CpwApplication.class, args);
    }
}
