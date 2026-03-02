package com.jsp.quiz_application.entity;

import java.util.Date;

public class AuthResponse {

    private String token;
    private String username;
    private Date issuedAt;
    private Date expiration;

    public AuthResponse(String token, String username, Date issuedAt, Date expiration) {
        this.token = token;
        this.username = username;
        this.issuedAt = issuedAt;
        this.expiration = expiration;
    }

    public String getToken() { return token; }
    public String getUsername() { return username; }
    public Date getIssuedAt() { return issuedAt; }
    public Date getExpiration() { return expiration; }
}