package com.will.gps;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.v4.app.FragmentManager;
import android.app.FragmentTransaction;

import com.google.gson.Gson;
import com.will.gps.base.DBOpenHelper;
import com.will.gps.base.MySocket;
import com.will.gps.base.RMessage;
import com.will.gps.bean.Group;
import com.will.gps.bean.GroupMember;
import com.will.gps.bean.MessageTabEntity;
import com.will.gps.bean.RecentContactBean;
import com.will.gps.bean.Signin;
import com.will.gps.bean.User;
import com.will.gps.layout.FirstFragment;
import com.will.gps.layout.GroupFragment;
import com.will.gps.layout.GroupMsgFragment;
import com.will.gps.layout.MessageFragment;
import com.will.gps.layout.RecentMsgFragment;
import com.will.gps.layout.UserFragment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    protected static final String TAG = "MainActivity";
    private TextView topBar;
    private TextView tabQun;
    private TextView tabMessage;
    private TextView unreadtip;
    private TextView tabMore;
    private TextView tabUser;
    private FrameLayout ly_content;

    private FirstFragment f4;
    private GroupFragment f1;
    private MessageFragment f2;
    private UserFragment f3;
    private ImageView imageView1, imageView2;
    private Intent intent;
    //private FragmentManager fragmentManager;
    private RMessage rMessage = new RMessage();
    private Gson gson = new Gson();
    /*FragmentManager fm=getSupportFragmentManager();
    FragmentTransaction ft=fm.beginTransaction();*/
    private List<RecentContactBean> List;//传到GroupMsgFragment
    private List<MessageTabEntity> List2;//传到RecentMsgFragment
    private List<String> groupList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bindView();
        rMessage.setType("登录成功");
        rMessage.setSenderphone(MySocket.user.getPhonenum());
        ((MySocket) getApplication()).send(gson.toJson(rMessage));

        //initHandler();

        // 开启通知栏，有信息的时候通知通知
        //NIMClient.toggleNotification(true);
        final DBOpenHelper dbOpenHelper = new DBOpenHelper(MainActivity.this);

        ((MySocket) getApplication()).setHandler(new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                rMessage = gson.fromJson(msg.obj.toString(), RMessage.class);
                String type = rMessage.getType();
                switch (type) {
                    case "更新信息":
                        MySocket.user = gson.fromJson(rMessage.getContent(), User.class);
                        dbOpenHelper.updateuser(dbOpenHelper);
                        break;
                    case "我的群":
                        dbOpenHelper.savegroup(dbOpenHelper, rMessage.getGroup());
                        tabMessage.performClick();
                        break;
                    case "群消息":
                        dbOpenHelper.savemsg(dbOpenHelper, rMessage);
                        break;
                    case "签到消息":
                        Signin signin = gson.fromJson(rMessage.getContent(),Signin.class);
                        dbOpenHelper.savesign(dbOpenHelper,signin);
                        break;
                    case "群成员":
                        dbOpenHelper.savemember(dbOpenHelper,rMessage.getGroup());
                        break;
                    default:
                        break;
                }
            }
        });
    }

    //UI组件初始化与事件绑定
    private void bindView() {
        FragmentTransaction transaction1 = getFragmentManager().beginTransaction();

        topBar = (TextView) this.findViewById(R.id.txt_top);
        imageView1 = (ImageView) findViewById(R.id.btn_search);
        imageView2 = (ImageView) findViewById(R.id.btn_add);
        tabQun = (TextView) this.findViewById(R.id.txt_qun);
        tabMessage = (TextView) this.findViewById(R.id.txt_message);
        unreadtip=(TextView)this.findViewById(R.id.unread_tip);
        tabUser = (TextView) this.findViewById(R.id.txt_user);
        tabMore = (TextView) this.findViewById(R.id.txt_more);
        ly_content = (FrameLayout) findViewById(R.id.fragment_container);
        /*Drawable drawable = getResources().getDrawable(R.drawable.menu_qun);
        // 设置图片的大小
        drawable.setBounds(0, 0, 2, 2);
        // 设置图片的位置，左、上、右、下
        tabQun.setCompoundDrawables(null, null, drawable, null);*/

        if(MySocket.unread=false)
            unreadtip.setVisibility(View.GONE);
        imageView1.setOnClickListener(this);
        imageView2.setOnClickListener(this);
        tabQun.setOnClickListener(this);
        tabMessage.setOnClickListener(this);
        tabMore.setOnClickListener(this);
        tabUser.setOnClickListener(this);
    }

    //重置所有文本的选中状态
    public void selected() {
        tabQun.setSelected(false);
        tabMessage.setSelected(false);
        tabUser.setSelected(false);
        tabMore.setSelected(false);
    }

    //隐藏所有Fragment
    public void hideAllFragment(FragmentTransaction transaction) {
        if (f1 != null) {
            transaction.hide(f1);
        }
        if (f2 != null) {
            transaction.hide(f2);
        }
        if (f3 != null) {
            transaction.hide(f3);
        }
        if (f4 != null) {
            transaction.hide(f4);
        }
    }

    @Override
    public void onClick(View v) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        hideAllFragment(transaction);
        switch (v.getId()) {
            case R.id.btn_search:
                intent = new Intent(MainActivity.this, SearchActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_add:
                intent = new Intent(MainActivity.this, CreateGroupActivity.class);
                startActivity(intent);
                break;
            case R.id.txt_qun:
                selected();
                tabQun.setSelected(true);
                topBar.setText("群");
                /*GroupMsgFragment groupMsgFragment=new GroupMsgFragment();
                ft.replace(R.id.fragment_container, groupMsgFragment,MainActivity.TAG);
                ft.commit();*/
                if (f1 == null) {
                    f1 = new GroupFragment();
                    transaction.add(R.id.fragment_container, f1);
                } else {
                    transaction.show(f1);
                }
                break;

            case R.id.txt_message:
                selected();
                tabMessage.setSelected(true);
                topBar.setText("消息");
                /*RecentMsgFragment recentMsgFragment=new RecentMsgFragment();
                ft.replace(R.id.fragment_container, recentMsgFragment,MainActivity.TAG);
                ft.commit();*/
                if (f2 == null) {
                    f2 = new MessageFragment();
                    transaction.add(R.id.fragment_container, f2);
                } else {
                    transaction.show(f2);
                }
                break;

            case R.id.txt_user:
                selected();
                tabUser.setSelected(true);
                topBar.setText("我的");
                /*UserFragment userFragment=new UserFragment();
                ft.replace(R.id.fragment_container, userFragment,MainActivity.TAG);
                ft.commit();*/
                if (f3 == null) {
                    f3 = new UserFragment();
                    transaction.add(R.id.fragment_container, f3);
                } else {
                    transaction.show(f3);
                }
                break;

            case R.id.txt_more:
                selected();
                tabMore.setSelected(true);
                topBar.setText("更多");
                /*FirstFragment firstFragment=new FirstFragment("界面待实现");
                ft.replace(R.id.fragment_container, firstFragment,MainActivity.TAG);
                ft.commit();*/
                if (f4 == null) {
                    f4 = new FirstFragment("第四个Fragment");
                    transaction.add(R.id.fragment_container, f4);
                } else {
                    transaction.show(f4);
                }
                break;
        }
        transaction.commit();
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        tabMessage.performClick();
        rMessage.setType("登录成功");
        rMessage.setSenderphone(MySocket.user.getPhonenum());
        ((MySocket) getApplication()).send(gson.toJson(rMessage));
    }

    //为了在fragment中注册监听事件（fragment中没有提供OnTouchEvent）
    //过程：定义接口，接口列表，activity的分发事件绑定给fragment，注册和注销
    //1.触摸事件接口
    public interface MyOnTouchListener {
        public boolean onTouch(View v, MotionEvent ev);
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
