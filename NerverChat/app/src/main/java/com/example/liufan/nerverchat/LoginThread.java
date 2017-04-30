package com.example.liufan.nerverchat;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketTimeoutException;

/**
 * Created by LIUFAN on 2017/4/23.
 */

public class LoginThread implements Runnable {
    private Socket socket;
    private Handler handler;
    public Handler revhandler;
    BufferedReader br =null;
    OutputStream os = null;
    public LoginThread(Handler handler){
        this.handler=handler;
    }
    public void run(){
        try{
            socket=new Socket("119.29.248.175",9400);
            br=new BufferedReader(new InputStreamReader(socket.getInputStream()));
            os=socket.getOutputStream();
            new Thread(){
                @Override
                public void run(){
                    String content=null;
                    try{
                        while((content=br.readLine())!=null){
                            if(content.equals("true")){
                                Message msg=new Message();
                                msg.what=0x123;
                                handler.sendMessage(msg);
                            }else if(content.equals("false")){
                                Message msg=new Message();
                                msg.what=0x1234;
                                handler.sendMessage(msg);
                            }
                        }
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                }
            }.start();
            Looper.prepare();
            revhandler=new Handler(){
                @Override
                public void handleMessage(Message msg){
                    if(msg.what==0x345){
                        try{
                            os.write((msg.obj.toString()+"\r").getBytes("utf-8"));
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }
            };
            Looper.loop();
        }catch (SocketTimeoutException e){
            e.printStackTrace();
            Message msg=new Message();
            msg.what=0x12345;
            handler.sendMessage(msg);
        }catch(Exception e){
            e.printStackTrace();
            Message msg=new Message();
            msg.what=0x12345;
            handler.sendMessage(msg);
        }
    }
}
