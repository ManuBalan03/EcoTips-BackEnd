package com.example.demo.JTW;

public class JtwResponse {
    private String token;
    private String type = "Bearer";

    public JtwResponse(String accessToken) {
        this.token = accessToken;
    }

    // Getters y setters
    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
}
