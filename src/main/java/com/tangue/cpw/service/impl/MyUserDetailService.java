package com.tangue.cpw.service.impl;

import com.tangue.cpw.repository.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;

@Service
@Slf4j
public class MyUserDetailService implements UserDetailsService {
    @Resource
    PasswordEncoder passwordEncoder;
    @Resource
    private UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        com.tangue.cpw.model.User user = this.userMapper.findByUsername(userName);
        System.out.println(user);
        MyUserDetails myUserDetails = new MyUserDetails(user);
        return myUserDetails;
        //return new User(user.getUsercode(), user.getPassword(), new ArrayList<>());
    }
}
