package com.example.liufan.nerverchat.db;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.liufan.nerverchat.db.ChatDBSchema.ChatTable;

/**
 * Created by LIUFAN on 2017/5/13.
 */

public class ChatBaseHelper extends SQLiteOpenHelper {
    private static final int VERSON = 1;
    private static final String DATABASE_NAME = "chatBase.db";
    public ChatBaseHelper(Context context){
        super(context,DATABASE_NAME ,null,VERSON);
    }
    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL("create table "+ ChatTable.NAME + "(" +
                " _id integer primary key autoincrement, " +
                ChatTable.Cols.USER + ", " +
                ChatTable.Cols.TONAME + ", " +
                ChatTable.Cols.CONTENT + ", " +
                ChatTable.Cols.ICON + ", " +
                ChatTable.Cols.TIME +
                ")"
        );
    }
    @Override
    public void onUpgrade(SQLiteDatabase db,int oldVersion, int newVersion){

    }
}
