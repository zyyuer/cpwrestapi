package com.tangue.cpw.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Data
public class User {
    private String usercode;
    private String username;
    private String password;
    private String roles = "";
    private String permissions = "";
    private String accountnonexpired;
    private String accountnonlocked;
    private String credentialsnonexpired;
    private String enabled;

    public List<String> getRoleList() {
        if (this.roles.length() > 0) {
            return Arrays.asList(this.roles.split(","));
        }
        return new ArrayList<>();
    }

    public List<String> getPermissionList() {
        if (this.permissions.length() > 0) {
            return Arrays.asList(this.permissions.split(","));
        }
        return new ArrayList<>();
    }
}
