package com.will.gps;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.will.gps.base.MySocket;
import com.will.gps.base.RMessage;
import com.will.gps.bean.GroupMember;
import com.will.gps.view.CircleImageView;

/**
 * Created by MaiBenBen on 2019/4/23.
 */

public class GroupInfoActivity extends Activity implements View.OnClickListener{
    private ImageView img1,img_back,img_more;
    private CircleImageView img2;
    private TextView textname,textnum,myname,member;
    private Button btn;
    public String isMember;
    public boolean isOwner=false;
    private RelativeLayout mynameview;
    private Intent i;
    RMessage rMessage = new RMessage();
    Gson gson = new Gson();
    GroupMember groupMember = new GroupMember();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_info);
        bindView();
        ((MySocket)getApplication()).setHandler(new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                rMessage=gson.fromJson(msg.obj.toString(),RMessage.class);
                String type = rMessage.getType();
                if(type.equals("加入群")||type.equals("解散群")||type.equals("退出群")){
                    if (rMessage.getContent().equals("true")){
                        Intent intent = new Intent(GroupInfoActivity.this,MainActivity.class);
                        startActivity(intent);
                    }else {
                        Toast.makeText(GroupInfoActivity.this, "操作失败", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
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
        textname.setText(i.getStringExtra("groupname"));
        textnum.setText(i.getStringExtra("groupid"));
        myname.setText(i.getStringExtra("groupowner"));
        member.setText(String.valueOf(i.getIntExtra("membernum",0))+"人");
        isMember=i.getStringExtra("ismember");

//        try{//判断是否是创建群
//            if(i.getStringExtra("type").equals("create")){
//                isOwner=true;
//                Toast.makeText(GroupInfoActivity.this,"欢迎群主",Toast.LENGTH_SHORT).show();
//            }else{
//                System.out.println("type为空");
//            }
//        }catch (Exception e){
//            System.out.println(e);
//        }

        if(!myname.getText().toString().equals(MySocket.user.getPhonenum())){//判读不是群主
            btn.setOnClickListener(this);
            if(isMember.equals("false")) {
                btn.setText("加群");
                btn.setTextColor(Color.BLUE);
                //mynameview.setVisibility(View.GONE);
            }else {
                btn.setText("退群");
                btn.setTextColor(Color.RED);
            }
        }else{
            btn.setOnClickListener(this);
            btn.setText("解散群");
            btn.setTextColor(Color.RED);
            //btn.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.group_info_btn:
                String bu=btn.getText().toString();
                if (bu.equals("加群"))//不是群成员
                {
                    groupMember.setGroupid(Integer.parseInt(textnum.getText().toString()));
                    groupMember.setGroupname(textname.getText().toString());
                    groupMember.setUsername(MySocket.user.getUserName());
                    groupMember.setUserphone(MySocket.user.getPhonenum());
                    rMessage.setContent(gson.toJson(groupMember));
                    rMessage.setGroupid(Integer.parseInt(textnum.getText().toString()));
                    rMessage.setType("加入群");
                    ((MySocket)getApplication()).send(gson.toJson(rMessage));
                } else if(bu.equals("退群")){
                    rMessage.setGroupid(Integer.parseInt(textnum.getText().toString()));
                    rMessage.setSenderphone(MySocket.user.getPhonenum());
                    rMessage.setType("退出群");
                    ((MySocket)getApplication()).send(gson.toJson(rMessage));
                }else if(bu.equals("解散群")){
                    rMessage.setGroupid(Integer.parseInt(textnum.getText().toString()));
                    rMessage.setSenderphone(MySocket.user.getPhonenum());
                    rMessage.setType("解散群");
                    ((MySocket)getApplication()).send(gson.toJson(rMessage));
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
