package com.example.liufan.nerverchat;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.liufan.nerverchat.Login.Util.LogUtil;
import com.example.liufan.nerverchat.Login.Util.ToastUtil;
import com.example.liufan.nerverchat.Login.custom.RippleView;
import com.example.liufan.nerverchat.Login.custom.StereoView;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText etUsername;
    private EditText etEmail;
    private EditText etPassword;
    private RippleView rvUsername;
    private RippleView rvEmail;
    private RippleView rvPassword;
    private StereoView stereoView;
    private LinearLayout lyWelcome;
    private TextView tvWelcome;
    private int translateY;
    private Handler handler;
    private LoginThread loginThread;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Toolbar toolbar=(Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("NeverChat");
        toolbar.setSubtitle("  Login now");
        toolbar.inflateMenu(R.menu.mini_menu);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener(){
            @Override
            public boolean onMenuItemClick(MenuItem item){
                int menuItemId=item.getItemId();
                Toast.makeText(LoginActivity.this,"click",Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        initView();
        stereoView.setStartScreen(2);
        stereoView.post(new Runnable() {
            @Override
            public void run() {
                int[] location = new int[2];
                stereoView.getLocationOnScreen(location);
                translateY = location[1];
            }
        });
        stereoView.setiStereoListener(new StereoView.IStereoListener() {
            @Override
            public void toPre(int curScreen) {
                LogUtil.m("跳转到前一页 " + curScreen);
            }

            @Override
            public void toNext(int curScreen) {
                LogUtil.m("跳转到下一页 " + curScreen);
            }
        });
        startHandler();
    }

    private void initView() {
        stereoView = (StereoView) findViewById(R.id.stereoView);
        etUsername = (EditText) findViewById(R.id.et_username);
        etEmail = (EditText) findViewById(R.id.et_email);
        etPassword = (EditText) findViewById(R.id.et_password);
        rvUsername = (RippleView) findViewById(R.id.rv_username);
        rvEmail = (RippleView) findViewById(R.id.rv_email);
        rvPassword = (RippleView) findViewById(R.id.rv_password);
        lyWelcome = (LinearLayout) findViewById(R.id.ly_welcome);
        tvWelcome = (TextView) findViewById(R.id.tv_welcome);
        rvUsername.setOnClickListener(this);
        rvEmail.setOnClickListener(this);
        rvPassword.setOnClickListener(this);
        tvWelcome.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rv_username:
                rvUsername.setiRippleAnimListener(new RippleView.IRippleAnimListener() {
                    @Override
                    public void onComplete(View view) {
                        stereoView.toPre();
                    }
                });
                break;
            case R.id.rv_email:
                rvEmail.setiRippleAnimListener(new RippleView.IRippleAnimListener() {
                    @Override
                    public void onComplete(View view) {
                        stereoView.toPre();
                    }
                });
                break;
            case R.id.rv_password:
                rvPassword.setiRippleAnimListener(new RippleView.IRippleAnimListener() {
                    @Override
                    public void onComplete(View view) {
                        stereoView.toPre();
                    }
                });
                /*rvPassword.setiRippleAnimListener(new RippleView.IRippleAnimListener() {
                    @Override
                    public void onComplete(View view) {
                        stereoView.toPre();
                    }
                });*/
                break;
            case R.id.tv_welcome:
                if (TextUtils.isEmpty(etUsername.getText())) {
                    ToastUtil.showInfo(LoginActivity.this, "请输入用户名!");
                    stereoView.setItem(2);
                    return;
                }/*
                if (TextUtils.isEmpty(etEmail.getText())) {
                    ToastUtil.showInfo(LoginActivity.this, "请输入邮箱!");
                    stereoView.setItem(0);
                    return;
                }*/
                if (TextUtils.isEmpty(etPassword.getText())) {
                    ToastUtil.showInfo(LoginActivity.this, "请输入密码!");
                    stereoView.setItem(1);
                    return;
                }
                loginLis();
                break;
        }
    }

    private void loginLis(){
        try{
            Message msg=new Message();
            String username=null;
            String password=null;
            username=etUsername.getText().toString();
            password=etPassword.getText().toString();
            msg.what=0x345;
            msg.obj=username+"\r"+password;
            loginThread.revhandler.sendMessage(msg);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    private void isLogin(){
        ObjectAnimator animator = ObjectAnimator.ofFloat(stereoView, "translationY", 0, 95, -translateY);
        animator.setDuration(500).start();
        ToastUtil.showInfo(LoginActivity.this, "登录成功");
        //启动新界面
        Intent intent = new Intent(LoginActivity.this,mini_drawer.class);
        startActivity(intent);
    }
    private void unLogin(){
        ToastUtil.showInfo(LoginActivity.this, "用户名或密码错误");
        //登陆失败
    }
    private void startHandler() {
        handler=new Handler(){
            @Override
            public void handleMessage(Message msg){
                if(msg.what==0x123){
                    isLogin();
                }if(msg.what==0x1234){
                    unLogin();
                }if(msg.what==0x12345){
                    ToastUtil.showInfo(LoginActivity.this, "服务器错误");
                }
            }
        };
        loginThread=new LoginThread(handler);
        new Thread(loginThread).start();
    }
}
