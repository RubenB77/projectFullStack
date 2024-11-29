package com.app.springBack.auth;

import lombok.Data;

@Data
public class AuthResponseDto {

    private String accessToken;

    public String getAccessToken() {
        return this.accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

}
