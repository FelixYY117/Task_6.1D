package com.example.task61d;

import java.util.List;
import okhttp3.OkHttpClient;
public class User {
    private String username;
    private String email;
    private String password;
    private List<String> interests;

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
        
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
