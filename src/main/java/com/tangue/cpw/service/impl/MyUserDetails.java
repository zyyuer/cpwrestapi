package com.tangue.cpw.service.impl;

import com.tangue.cpw.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MyUserDetails implements UserDetails {
    private User user;

    public MyUserDetails(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        this.user.getPermissionList().forEach(p -> {
            GrantedAuthority authority = new SimpleGrantedAuthority(p);
            authorities.add(authority);
        });

        this.user.getRoleList().forEach(r -> {
            GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + r);
            authorities.add(authority);
        });
        return authorities;
    }

    @Override
    public String getPassword() {
        return this.user.getPassword();
    }

    @Override
    public String getUsername() {
        return this.user.getUsercode();
    }

    @Override
    public boolean isAccountNonExpired() {
        return "T".equals(this.user.getAccountnonexpired());
    }

    @Override
    public boolean isAccountNonLocked() {
        return "T".equals(this.user.getAccountnonlocked());
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return "T".equals(this.user.getCredentialsnonexpired());
    }

    @Override
    public boolean isEnabled() {
        return "T".equals(this.user.getEnabled());
    }
}
