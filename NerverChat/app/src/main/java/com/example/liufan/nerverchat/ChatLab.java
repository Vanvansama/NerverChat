package com.example.liufan.nerverchat;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LIUFAN on 2017/5/6.
 */

public class ChatLab {
    private static ChatLab sChatLab;

    private List<Chat> mChats;
    public static ChatLab get(Context context){
        if (sChatLab == null){
            sChatLab = new ChatLab(context);
        }
        return sChatLab;
    }
    private ChatLab(Context context){
        mChats = new ArrayList<>();
        for (int i = 0; i < 20; i++){
            Chat chat = new Chat();
            chat.setName("item "+i);
            mChats.add(chat);
        }
    }
    public List<Chat> getChats(){
        return mChats;
    }
    public Chat getChat(int i){
        return null;
    }
}
