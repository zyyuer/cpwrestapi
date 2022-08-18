package com.tangue.cpw;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.annotation.Resource;

@SpringBootTest
class CpwApplicationTests {
    @Resource
    private PasswordEncoder passwordEncoder;
    @Test
    void contextLoads() {
    }
    @Test
    public void encode() {
        System.out.println(passwordEncoder.encode("00"));
        System.out.println(passwordEncoder.matches("00","$2a$10$icsB1lGnlvi4oIkGaRF3XeberNuLOLJPZI0NqItslQDm7j.N6VaQe"));
    }
}
