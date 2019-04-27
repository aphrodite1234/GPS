package com.will.gps.base;
import android.app.Application;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.Toast;
import com.google.gson.Gson;
import com.will.gps.bean.User;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class MySocket extends Application{

    private Handler handler = new Handler(Looper.getMainLooper());
    private Socket socket=null;
    private PrintWriter writer;
    private BufferedReader reader;
    private static final String SERVER_IP="188.131.189.2";
    private static final int PORT=5678;
    private static final long HEART_BEAT_RATE = 5 * 1000;//心跳间隔
    private long sendTime = 0L;
    public static User user = new User();
    private RMessage rMessage=new RMessage();
    private Gson gson = new Gson();

    public void setHandler(Handler handler) {
        this.handler = handler;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        connectServer();
    }

    public void connectServer(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                //与服务器建立连接
                try {
                    socket = new Socket(SERVER_IP, PORT);
                    writer = new PrintWriter(socket.getOutputStream(),true);
                    reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                } catch (Exception e) {
                    msgToast("服务器连接失败！");
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void sendHeart(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    if (System.currentTimeMillis() - sendTime >= HEART_BEAT_RATE) {
                        rMessage.setType("心跳");
                        boolean isSuccess = sendHeart1(gson.toJson(rMessage));
                        if (!isSuccess) {
                            msgToast("发送失败！");
                            release();
                        }
                    }
                }
            }
        }).start();
    }

    private void release(){//释放资源
        if (socket != null) {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        socket=null;
        connectServer();
    }

    private void msgToast(final String string){
        handler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(),string,Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean sendHeart1(String msg){//发送心跳消息
        if(socket != null&&!socket.isClosed()&&!socket.isOutputShutdown()){
            writer.println(msg);
            sendTime=System.currentTimeMillis();
            return true;
        }else{
            return false;
        }
    }

    public void read(){//接受消息
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String line=null;
                    while((line=reader.readLine())!=null){
                        Message msg = Message.obtain();
                        msg.what=1;
                        msg.obj=line;
                        handler.sendMessage(msg);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void send(final String string){//发送
        new Thread(new Runnable() {
            @Override
            public void run() {
                if(string!=null && socket.isConnected()){
                    writer.println(string);
                }else {
                    release();
                }
            }
        }).start();
    }
}