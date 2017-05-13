package com.example.liufan.nerverchat;

import android.content.Context;

import com.mikepenz.google_material_typeface_library.GoogleMaterial;

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
        Chat chat = new Chat();
        chat.setName("Yuki");
        chat.setImageID(R.drawable.yuki);
        mChats.add(chat);
        for (int i = 0; i < 20; i++){
            Chat c = new Chat();
            c.setName("item "+i);
            mChats.add(c);
        }
    }
    public List<Chat> getChats(){
        return mChats;
    }
    public Chat getChat(int i){
        return null;
    }
}
