package com.example.liufan.nerverchat.db;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.example.liufan.nerverchat.ChatModel;

import static com.example.liufan.nerverchat.db.ChatDBSchema.*;

/*
 * Created by LIUFAN on 2017/5/13.
 */

public class ChatMessageCursorWrapper extends CursorWrapper {
    public ChatMessageCursorWrapper(Cursor cursor){
        super(cursor);
    }
    public ChatModel getChatMessage(){
        String user = getString(getColumnIndex(ChatTable.Cols.USER ));
        String name = getString(getColumnIndex(ChatTable.Cols.TONAME ));
        String content = getString(getColumnIndex(ChatTable.Cols.CONTENT ));
        int icon = getInt(getColumnIndex(ChatTable.Cols.ICON));
        long time = getLong(getColumnIndex(ChatTable.Cols.TIME));

        ChatModel model = new ChatModel();
        model.setUser(user);
        model.setName(name);
        model.setContent(content);
        model.setIcon(icon);
        model.setDate(time);

        return model;
    }
}

