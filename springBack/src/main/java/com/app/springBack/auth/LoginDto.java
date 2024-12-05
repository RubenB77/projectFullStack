package com.app.springBack.auth;

import lombok.Data;

@Data
public class LoginDto {

    private String username;
    private String password;

    public String getUsername(){
        return this.username;
    }

    public String getPassword(){
        return this.password;
    }

    public LoginDto(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
