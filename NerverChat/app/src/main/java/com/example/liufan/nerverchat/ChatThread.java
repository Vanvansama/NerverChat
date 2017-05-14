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
 * Created by LIUFAN on 2017/5/14.
 */

public class ChatThread implements Runnable {
    private Socket socket;
    private Handler handler;
    public Handler revhandler;
    BufferedReader br =null;
    OutputStream os = null;
    public ChatThread(Handler handler){
        this.handler = handler;
    }
    public void run(){
        try{
            socket=new Socket("119.29.117.108",9500);
            br=new BufferedReader(new InputStreamReader(socket.getInputStream()));
            os=socket.getOutputStream();
            new Thread(){
                @Override
                public void run(){
                    String content = null;
                    try{
                        while((content=br.readLine())!=null){
                            Message msg = new Message();
                            msg.what = 0x123;
                            msg.obj = content;
                            handler.sendMessage(msg);
                        }
                    }catch(IOException e){
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
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
