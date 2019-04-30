package com.will.gps;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.will.gps.view.CircleImageView;

/**
 * Created by MaiBenBen on 2019/4/23.
 */

public class GroupInfoActivity extends Activity implements View.OnClickListener{
    private ImageView img1,img_back,img_more;
    private CircleImageView img2;
    private TextView textname,textnum,myname,member;
    private Button btn;
    private boolean isMember=false;
    public boolean isOwner=false;
    private RelativeLayout mynameview;
    private Intent i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_info);
        bindView();
    }

    private void bindView(){
        img1=(ImageView)findViewById(R.id.group_info_img1);
        img2=(CircleImageView) findViewById(R.id.group_info_img2);
        textname=(TextView)findViewById(R.id.group_info_txtname);
        textnum=(TextView)findViewById(R.id.group_info_txtnum);
        myname=(TextView)findViewById(R.id.group_info_myname1);
        member=(TextView)findViewById(R.id.group_info_member1);
        btn=(Button)findViewById(R.id.group_info_btn);
        mynameview=(RelativeLayout)findViewById(R.id.group_info_layout_myname);
        img_back=(ImageView)findViewById(R.id.group_info_back);
        img_more=(ImageView)findViewById(R.id.group_info_more);
        img_back.setOnClickListener(this);
        img_more.setOnClickListener(this);

        i=getIntent();
        try{//判断是否是创建群
            if(i.getStringExtra("type").equals("create")){
                isOwner=true;
                Toast.makeText(GroupInfoActivity.this,"欢迎群主",Toast.LENGTH_SHORT).show();
                textname.setText(i.getStringExtra("groupname"));
                textnum.setText(i.getStringExtra("groupid"));
                myname.setText("群主");
                member.setText("1人");
            }else{
                System.out.println("type为空");
            }
        }catch (Exception e){
            System.out.println(e);
        }

        if(!isOwner){//判读不是群主
            btn.setOnClickListener(this);
            if(!isMember) {
                btn.setText("申请加群");
                btn.setTextColor(Color.BLUE);
                mynameview.setVisibility(View.GONE);
            }else {
                btn.setText("退群");
                btn.setTextColor(Color.RED);
            }
        }else{
            btn.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.group_info_btn:
                if (!isMember)//不是群成员
                {
                    Toast.makeText(GroupInfoActivity.this, "申请发送成功！", Toast.LENGTH_SHORT).show();
                } else//已经是群成员
                {

                }
                break;
            case R.id.group_info_back:
                finish();
                break;
            case R.id.group_info_more:
                Toast.makeText(GroupInfoActivity.this,"功能未实现",Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
