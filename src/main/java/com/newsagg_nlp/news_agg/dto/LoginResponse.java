package com.newsagg_nlp.news_agg.dto;

public class LoginResponse {

    private String jwtToken;
    private String username;
    private  String userId;

    public LoginResponse(String username, String jwtToken,String userId) {
        this.username = username;
        this.jwtToken = jwtToken;
        this.userId=userId;
    }

    public String getJwtToken() {
        return jwtToken;
    }

    public void setJwtToken(String jwtToken) {
        this.jwtToken = jwtToken;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserId() {return userId;}

    public void setUserId(String userId) {this.userId = userId;}
}
