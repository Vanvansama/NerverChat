package com.example.liufan.nerverchat;

import java.io.Serializable;


public class ItemModel implements Serializable {

    public static final int CHAT_From = 1001;
    public static final int CHAT_To = 1002;
    public int type;
    public Object object;

    public ItemModel(int type, Object object) {
        this.type = type;
        this.object = object;
    }
}
