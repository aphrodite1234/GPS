package com.will.gps.Base;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.will.gps.LoadActivity;
import com.will.gps.MainActivity;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.net.Socket;
import java.security.MessageDigest;

/**
 * Created by MaiBenBen on 2019/3/26.
 */

public class ClientThread implements Runnable {//客户端接收发送信息的线程类
    Handler handler;
    public Handler pushHandler;
    private Socket s;
    BufferedReader br;
    private OutputStream os;
    public static final int SEND = 33;
    //private PrintWriter writer;

    public ClientThread(Handler handler) {
        this.handler = handler;
    }

    private static final String SERVER_IP = "188.131.189.2";
    private static final int PORT = 5678;

    public static String getMD5String(String str) {
        try {
            // 生成一个MD5加密计算摘要
            MessageDigest md = MessageDigest.getInstance("MD5");
            // 计算md5函数
            md.update(str.getBytes());
            // digest()最后确定返回md5 hash值，返回值为8位字符串。因为md5 hash值是16位的hex值，实际上就是8位的字符
            // BigInteger函数则将8位的字符串转换成16位hex值，用字符串来表示；得到字符串形式的hash值
            //一个byte是八位二进制，也就是2位十六进制字符（2的8次方等于16的2次方）
            return new BigInteger(1, md.digest()).toString(16);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void run() {
        try {
            s = new Socket(SERVER_IP, PORT);//Socket地址端口
            br = new BufferedReader(new InputStreamReader(s.getInputStream()));
            os = s.getOutputStream();

            new Thread() {
                public void run() {
                    String line = null;
                    try {
                        while ((line = br.readLine()) != null) {
                            Message msg = new Message();
                            if(line.equals("true")){
                                msg.what = LoadActivity.DL;
                                msg.obj = line;
                                handler.sendMessage(msg);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }.start();
            startpush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void startpush() {
        Looper.prepare();
        pushHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case SEND:
                        try {
                            /*
                             *//*os.write(content.getBytes("utf-8"));*//*
                            writer=new PrintWriter(s.getOutputStream(),true);
                            String content=msg.obj.toString()+"\r\n";
                            writer.println(content);*/
                            String content = msg.obj.toString() + "\r\n";
                            os.write(content.getBytes("utf-8"));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    default:
                        break;
                }
                super.handleMessage(msg);
            }
        };
        Looper.loop();
    }
}
