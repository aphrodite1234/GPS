package com.will.gps;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.netease.nimlib.sdk.NIMClient;
import com.suntek.commonlibrary.utils.ToastUtils;
import com.will.gps.handler.NimFriendHandler;
import com.will.gps.handler.NimOnlineStatusHandler;
import com.will.gps.handler.NimSysMsgHandler;
import com.will.gps.handler.NimUserHandler;
import com.will.gps.layout.FirstFragment;
import com.will.gps.layout.RecentMsgFragment;
import com.will.gps.layout.UserFragment;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView topBar;
    private TextView tabQun;
    private TextView tabMessage;
    private TextView tabMore;
    private TextView tabUser;

    private FrameLayout ly_content;

    private FirstFragment f2,f4;
    private RecentMsgFragment f1;
    private UserFragment f3;
    private ImageView imageView1,imageView2;
    //private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bindView();
        //initHandler();

        // 开启通知栏，有信息的时候通知通知
        //NIMClient.toggleNotification(true);
    }
    //UI组件初始化与事件绑定
    private void bindView() {
        topBar = (TextView)this.findViewById(R.id.txt_top);
        imageView1=(ImageView)findViewById(R.id.btn_search);
        imageView2=(ImageView)findViewById(R.id.btn_add);
        tabQun = (TextView)this.findViewById(R.id.txt_qun);
        tabMessage = (TextView)this.findViewById(R.id.txt_message);
        tabUser = (TextView)this.findViewById(R.id.txt_user);
        tabMore = (TextView)this.findViewById(R.id.txt_more);
        ly_content = (FrameLayout) findViewById(R.id.fragment_container);

        /*Drawable drawable = getResources().getDrawable(R.drawable.menu_qun);
        // 设置图片的大小
        drawable.setBounds(0, 0, 2, 2);
        // 设置图片的位置，左、上、右、下
        tabQun.setCompoundDrawables(null, null, drawable, null);*/

        tabQun.setSelected(true);
        imageView1.setOnClickListener(this);
        imageView2.setOnClickListener(this);
        tabQun.setOnClickListener(this);
        tabMessage.setOnClickListener(this);
        tabMore.setOnClickListener(this);
        tabUser.setOnClickListener(this);

    }

    //重置所有文本的选中状态
    public void selected(){
        tabQun.setSelected(false);
        tabMessage.setSelected(false);
        tabUser.setSelected(false);
        tabMore.setSelected(false);
    }

    //隐藏所有Fragment
    public void hideAllFragment(FragmentTransaction transaction){
        if(f1!=null){
            transaction.hide(f1);
        }
        if(f2!=null){
            transaction.hide(f2);
        }
        if(f3!=null){
            transaction.hide(f3);
        }
        if(f4!=null){
            transaction.hide(f4);
        }
    }

    @Override
    public void onClick(View v) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        hideAllFragment(transaction);
        switch(v.getId()){
            case R.id.btn_search:

            case R.id.txt_qun:
                selected();
                tabQun.setSelected(true);
                topBar.setText("进群");
                if(f1==null){
                    f1 = new RecentMsgFragment(this);
                    transaction.add(R.id.fragment_container,f1);
                }else{
                    transaction.show(f1);
                }
                break;

            case R.id.txt_message:
                selected();
                tabMessage.setSelected(true);
                topBar.setText("消息");
                if(f2==null){
                    f2 = new FirstFragment("第二个Fragment");
                    transaction.add(R.id.fragment_container,f2);
                }else{
                    transaction.show(f2);
                }
                break;

            case R.id.txt_user:
                selected();
                tabUser.setSelected(true);
                topBar.setText("我的");
                if(f3==null){
                    f3 = new UserFragment(this);
                    transaction.add(R.id.fragment_container,f3);
                }else{
                    transaction.show(f3);
                }
                break;

            case R.id.txt_more:
                selected();
                tabMore.setSelected(true);
                topBar.setText("更多");
                if(f4==null){
                    f4 = new FirstFragment("第四个Fragment");
                    transaction.add(R.id.fragment_container,f4);
                }else{
                    transaction.show(f4);
                }
                break;
        }
        transaction.commit();
    }

    private void initHandler() {
        NimOnlineStatusHandler.getInstance().init();
        NimOnlineStatusHandler.getInstance().setStatusChangeListener(
                new NimOnlineStatusHandler.OnStatusChangeListener() {
                    @Override
                    public void requestReLogin(String message) {
                        ToastUtils.showMessage(MainActivity.this,"自动登陆失败或被踢出，请手动登陆~");
                        startActivity(new Intent(MainActivity.this,LoadActivity.class));
                    }

                    @Override
                    public void networkBroken() {

                    }
                });

        NimSysMsgHandler.getInstance().init();
        NimFriendHandler.getInstance().init();
        NimUserHandler.getInstance().init();
    }
    //为了在fragment中注册监听事件（fragment中没有提供OnTouchEvent）
    //过程：定义接口，接口列表，activity的分发事件绑定给fragment，注册和注销
    //1.触摸事件接口
    public interface MyOnTouchListener {
        public boolean onTouch(View v,MotionEvent ev);
    }
    //2. 保存MyOnTouchListener接口的列表
    private ArrayList<MyOnTouchListener> onTouchListeners = new ArrayList<MyOnTouchListener>();
    //3.分发触摸事件给所有注册了MyOnTouchListener的接口
    /*@Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        for (MyOnTouchListener listener : onTouchListeners) {
            if(listener != null) {
                listener.onTouch(ev);
            }
        }
        return super.dispatchTouchEvent(ev);
    }*/
    //4.提供给Fragment通过getActivity()方法来注册自己的触摸事件的方法
    public void registerMyOnTouchListener(MyOnTouchListener myOnTouchListener) {
        onTouchListeners.add(myOnTouchListener);
    }
    //5.提供给Fragment通过getActivity()方法来注销自己的触摸事件的方法
    public void unregisterMyOnTouchListener(MyOnTouchListener myOnTouchListener) {
        onTouchListeners.remove(myOnTouchListener);
    }
    //可以把fragment看成是Activity中的一个子view，所以，触摸屏幕的时候，首先会触发
    // dispatchTouchEvent函数，在这个函数里面为fragment设置触摸方法
    //fragment具体代码：
    //过程：初始化监听器，注册和注销，同时监听器中监听手势动作
}
