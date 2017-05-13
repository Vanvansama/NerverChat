package com.example.liufan.nerverchat;

import java.io.Serializable;

/**
 * Created by LIUFAN on 2017/5/13.
 */

public class user implements Serializable {
    String username;
    String password;

    user(String username,String password){
        this.username=username;
        this.password=password;
    }
    user(){

    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
