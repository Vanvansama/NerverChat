package com.example.liufan.nerverchat;

import android.annotation.TargetApi;
import android.content.ClipData;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.liufan.nerverchat.db.ChatBaseHelper;
import com.example.liufan.nerverchat.db.ChatDBSchema;
import com.example.liufan.nerverchat.db.ChatMessageCursorWrapper;
import com.github.anzewei.parallaxbacklayout.ParallaxActivityBase;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import static com.example.liufan.nerverchat.db.ChatDBSchema.*;


public class ChatLayout extends ParallaxActivityBase {

    private RecyclerView recyclerView;
    private EditText etMessage;
    private TextView btSend;
    private String content;
    private ChatLayoutAdapter adapter;
    private user user;
    private Chat chat;
    private ArrayList<ChatModel> chatModels = new ArrayList<>();
    private ArrayList<ItemModel> itemModels = new ArrayList<>();
    private SQLiteDatabase database;
    private ChatBaseHelper helper;
    private Handler handler;
    private ChatThread thread;
    int imageFrom;
    int imageTo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            //设置顶部状态栏颜色
            window.setStatusBarColor(Color.TRANSPARENT);
            //设置底部导航栏颜色(有的手机没有)
            window.setNavigationBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_chat_layout);

        //Toolbar
        Toolbar toolbar=(Toolbar) findViewById(R.id.chatToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);

        //intent data
        Intent intent = getIntent();
        user = (user) intent.getSerializableExtra("user");
        chat = (Chat) intent.getSerializableExtra("chat");
        if((user.getUsername().equals("root"))&&(chat.getName().equals("Yuki"))){
            imageTo=R.drawable.user;imageFrom=R.drawable.yuki;
        }else if((user.getUsername().equals("yuki"))&&(chat.getName().equals("Root"))){
            imageTo=R.drawable.yuki;imageFrom=R.drawable.user;
        }

        //connected databases
        helper = new ChatBaseHelper(this);
        database = helper.getReadableDatabase();
        initDB();

        //
        recyclerView = (RecyclerView) findViewById(R.id.chatLayout_recyclerView);
        etMessage = (EditText) findViewById(R.id.etMessage);
        btSend = (TextView) findViewById(R.id.btSend);

        //recycler
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        recyclerView.setAdapter(adapter = new ChatLayoutAdapter());
        adapter.replaceAll(itemModels);

        //handle message
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg){
                if(msg.what == 0x123){
                    String content = (String) msg.obj;
                    ArrayList<ItemModel> data = new ArrayList<>();
                    ChatModel model = new ChatModel();
                    model.setName(chat.getName());
                    model.setIcon(imageFrom);
                    model.setContent(content);
                    data.add(new ItemModel(ItemModel.CHAT_From, model));
                    adapter.addAll(data);
                    addChatMessage(model);
                }
            }
        };
        thread = new ChatThread(handler);
        new Thread(thread).start();
        initData();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        if(helper!=null)
            helper.close();
    }

    private void initData() {
        etMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                content = s.toString().trim();
            }
        });

        btSend.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                Message msg = new Message();
                msg.what = 0x345;
                msg.obj = content;
                thread.revhandler.sendMessage(msg);

                ArrayList<ItemModel> data = new ArrayList<>();
                ChatModel model = new ChatModel();
                model.setUser(user.getUsername());
                model.setIcon(imageTo);
                model.setContent(content);
                data.add(new ItemModel(ItemModel.CHAT_To, model));
                adapter.addAll(data);
                addChatMessage(model);
                etMessage.setText("");
                hideKeyBorad(etMessage);
            }
        });

    }

    private void hideKeyBorad(View v) {
        InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
            imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
        }
    }

    private static ContentValues getContentValues(ChatModel model){
        long time;
        ContentValues values = new ContentValues();
        values.put(ChatTable.Cols.USER ,model.getUser().toString());
        values.put(ChatTable.Cols.TONAME ,model.getName().toString());
        values.put(ChatTable.Cols.CONTENT ,model.getContent().toString());
        values.put(ChatTable.Cols.ICON ,model.getIcon());
        values.put(ChatTable.Cols.TIME, time=System.currentTimeMillis());
        return values;
    }//键值对
    public void addChatMessage(ChatModel model){
        ContentValues values = getContentValues(model);
        database.insert(ChatTable.NAME ,null ,values);
    }//写入数据库
    private ChatMessageCursorWrapper queryChatMessage(String whereClause, String[] whereArgs){
        Cursor cursor = database.query(
                ChatTable.NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null
        );
        return new ChatMessageCursorWrapper(cursor);
    }//查询语句
    public ArrayList<ChatModel> getChatMessages(){
        ArrayList<ChatModel> models = new ArrayList<>();
        ChatMessageCursorWrapper cursor = queryChatMessage(null,null);
        try{
            cursor.moveToFirst();
            while(!cursor.isAfterLast()){
                models.add(cursor.getChatMessage());
                cursor.moveToNext();
            }
        }finally {
            cursor.close();
        }
        return models;
    }//查询到的list
    private void initDB(){
        chatModels.clear();
        itemModels.clear();

        ChatModel model = new ChatModel();
        chatModels = getChatMessages();
        Iterator it = chatModels.iterator();
        while(it.hasNext()){
            model = (ChatModel) it.next();
            if((model.getUser().equals(""))&&(!model.getName().equals(""))) {
                itemModels.add(new ItemModel(ItemModel.CHAT_From, model));
            }else if((!model.getUser().equals(""))&&(model.getName().equals(""))){
                itemModels.add(new ItemModel(ItemModel.CHAT_To, model));
            }
        }
    }


}
