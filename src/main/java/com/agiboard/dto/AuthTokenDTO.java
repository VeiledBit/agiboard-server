package com.agiboard.dto;

public class AuthTokenDTO {
    private String token;
    private String username;

    public AuthTokenDTO() {
    }

    public AuthTokenDTO(String token, String username) {
        this.token = token;
        this.username = username;
    }

    public AuthTokenDTO(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
