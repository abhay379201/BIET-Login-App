package com.example.abhay.biet_login;

import java.io.Serializable;

/**
 * Created by abhay on 26/11/17.
 */

public class UserData implements Serializable {
    private String username,password;
    private boolean status;

    public UserData(String username, String password, boolean status) {
        this.username = username;
        this.password = password;
        this.status = status;
    }


    public String getUsername() {
        return username;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
