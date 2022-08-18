package com.tangue.cpw.repository;

import com.tangue.cpw.model.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMapper {
    User findByUsername(String username);
}
