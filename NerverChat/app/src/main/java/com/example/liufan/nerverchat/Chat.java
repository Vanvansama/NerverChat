package com.example.liufan.nerverchat;

import java.io.Serializable;

/**
 * Created by LIUFAN on 2017/5/6.
 */

public class Chat implements Serializable{
    String user;
    String name;
    int imageID = 0;

    public int getImageID() {
        return imageID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImageID(int imageID) {
        this.imageID = imageID;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
