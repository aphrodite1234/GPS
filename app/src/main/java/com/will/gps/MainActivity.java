package com.will.gps;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import org.json.*;

import com.will.gps.Base.ClientThread;

import static com.will.gps.Base.ClientThread.getMD5String;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView topBar;
    private TextView tabQun;
    private TextView tabMessage;
    private TextView tabMore;
    private TextView tabUser;

    private FrameLayout ly_content;

    private FirstFragment f1,f2,f3,f4;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bindView();

    }
    //UI组件初始化与事件绑定
    private void bindView() {
        topBar = (TextView)this.findViewById(R.id.txt_top);
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
            case R.id.txt_qun:
                selected();
                tabQun.setSelected(true);
                topBar.setText("进群");
                if(f1==null){
                    f1 = new FirstFragment("第一个Fragment");
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
                    f3 = new FirstFragment("第三个Fragment");
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
}
