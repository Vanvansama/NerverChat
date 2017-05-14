package com.example.liufan.nerverchat.db;

/**
 * Created by LIUFAN on 2017/5/13.
 */

public class ChatDBSchema {
    public static final class ChatTable{
        public static final String NAME = "chatMessage";

        public static final class Cols{
            public static final String USER = "user";
            public static final String TONAME = "name";
            public static final String CONTENT = "content";
            public static final String ICON = "icon";
        }
    }

}
