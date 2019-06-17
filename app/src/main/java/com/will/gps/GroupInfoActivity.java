package com.will.gps;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.will.gps.base.DBOpenHelper;
import com.will.gps.base.MySocket;
import com.will.gps.base.RMessage;
import com.will.gps.bean.GroupMember;
import com.will.gps.bean.Signin;
import com.will.gps.map.AmapActivity;
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
    private RelativeLayout mynameview;
    private Intent i;
    private PopupMenu popupMenu;
    private Menu menu;
    RMessage rMessage = new RMessage();
    Gson gson = new Gson();
    GroupMember groupMember = new GroupMember();
    private boolean havesign = false;
    int groupid;
    int id;
    String groupowner;
    Signin signin = new Signin();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_info);

        i=getIntent();
        groupid=i.getIntExtra("groupid",0);
        groupowner=i.getStringExtra("groupowner");
        final DBOpenHelper dbOpenHelper = new DBOpenHelper(GroupInfoActivity.this);
        SQLiteDatabase db=dbOpenHelper.getReadableDatabase();
        Cursor cursor = db.query("signin", null, "groupid='"+groupid+"' AND state=0 AND originator="+groupowner, null, null, null, null);
        if(cursor.getCount()!=0){
            cursor.moveToNext();
            signin.setId(cursor.getInt(cursor.getColumnIndex("id")));
            signin.setGroupid(groupid);
            signin.setTime(cursor.getString(cursor.getColumnIndex("time")));
            havesign=true;
            System.out.println(gson.toJson(signin));
        }else {
            havesign=false;
        }

        bindView();
        ((MySocket)getApplication()).setHandler(new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                Gson gson = new Gson();
                Signin signin = new Signin();
                RMessage rMessage = gson.fromJson(msg.obj.toString(), RMessage.class);
                String type = rMessage.getType();
                if(type.equals("加入群")||type.equals("解散群")||type.equals("退出群")){
                    if (rMessage.getContent().equals("true")){
                        Intent intent = new Intent(GroupInfoActivity.this,MainActivity.class);
                        startActivity(intent);
                    }else {
                        Toast.makeText(GroupInfoActivity.this, "操作失败", Toast.LENGTH_SHORT).show();
                    }
                }
                switch (type){
                    case "群消息":
                        dbOpenHelper.savemsg(dbOpenHelper, rMessage);
                        break;
                    case "签到消息":
                        signin = gson.fromJson(rMessage.getContent(),Signin.class);
                        dbOpenHelper.savesign(dbOpenHelper,signin);
                        break;
                    case "解散群":
                        dbOpenHelper.deletegroup(dbOpenHelper,rMessage.getGroupid());
                        break;
                    case "用户签到":
                        signin=gson.fromJson(rMessage.getContent(),Signin.class);
                        if(signin.getDone()==1){
                            dbOpenHelper.updatesignin(dbOpenHelper,signin.getId());
                        }
                        break;
                    case "签到截止":
                        dbOpenHelper.endsign(dbOpenHelper,gson.fromJson(rMessage.getContent(),Signin.class));
                        break;
                    default:
                        break;
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

        popupMenu=new PopupMenu(this,findViewById(R.id.group_info_more));
        menu = popupMenu.getMenu();
        // 通过代码添加菜单项
        /*menu.add(Menu.NONE, Menu.FIRST + 0, 0, "复制");
        menu.add(Menu.NONE, Menu.FIRST + 1, 1, "粘贴");*/
        //通过XML文件添加菜单项
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_group_info, menu);

        img_back.setOnClickListener(this);
        img_more.setOnClickListener(this);

        textname.setText(i.getStringExtra("groupname"));
        textnum.setText(groupid+"");
        myname.setText(groupowner);
        member.setText(String.valueOf(i.getIntExtra("membernum",0))+"人");
        isMember=i.getStringExtra("ismember");

        if(!myname.getText().toString().equals(MySocket.user.getPhonenum())){//判读不是群主
            popupMenu.getMenu().findItem(R.id.menu_groupinfo_startsign).setVisible(false);
            popupMenu.getMenu().findItem(R.id.menu_groupinfo_endsign).setVisible(false);
            //if(!havesign) popupMenu.getMenu().findItem(R.id.menu_groupinfo_signlist).setVisible(false);
            btn.setOnClickListener(this);
            if(isMember.equals("false")) {
                btn.setText("加群");
                btn.setTextColor(Color.BLUE);
                //mynameview.setVisibility(View.GONE);
            }else {
                btn.setText("退群");
                btn.setTextColor(Color.RED);
            }

            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.menu_groupinfo_signlist:
                            //Toast.makeText(GroupInfoActivity.this,"点击签到列表！",Toast.LENGTH_SHORT).show();
                            Intent intent=new Intent(GroupInfoActivity.this,SignTableListActivity.class);
                            intent.putExtra("groupid",textnum.getText().toString());
                            intent.putExtra("groupowner",myname.getText().toString());
                            startActivity(intent);
                        default:
                            break;
                    }
                    return false;
                }
            });
        }else{
            if(havesign){//判断有没有签到活动
                popupMenu.getMenu().findItem(R.id.menu_groupinfo_startsign).setVisible(false);
            }else{
                popupMenu.getMenu().findItem(R.id.menu_groupinfo_endsign).setVisible(false);
            }
            // 监听事件
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.menu_groupinfo_startsign:
                            //Toast.makeText(GroupInfoActivity.this, "点击发起签到！", Toast.LENGTH_SHORT).show();
                            Intent i=new Intent(GroupInfoActivity.this,CreateSignActivity.class);
                            i.putExtra("groupid",textnum.getText().toString());
                            startActivity(i);
                            popupMenu.getMenu().findItem(R.id.menu_groupinfo_startsign).setVisible(false);
                            popupMenu.getMenu().findItem(R.id.menu_groupinfo_endsign).setVisible(true);
                            break;
                        case R.id.menu_groupinfo_endsign:
                            //Toast.makeText(GroupInfoActivity.this, "点击结束签到！", Toast.LENGTH_SHORT).show();
                            RMessage message = new RMessage();
                            message.setType("签到截止");
                            message.setContent(gson.toJson(signin));
                            ((MySocket)getApplication()).send(gson.toJson(message));
                            popupMenu.getMenu().findItem(R.id.menu_groupinfo_startsign).setVisible(true);
                            popupMenu.getMenu().findItem(R.id.menu_groupinfo_endsign).setVisible(false);
                            DBOpenHelper dbOpenHelper = new DBOpenHelper(GroupInfoActivity.this);
                            dbOpenHelper.endsign(dbOpenHelper,signin);
                            break;
                        case R.id.menu_groupinfo_signlist:
                            //Toast.makeText(GroupInfoActivity.this,"点击签到列表！",Toast.LENGTH_SHORT).show();
                            Intent intent=new Intent(GroupInfoActivity.this,SignTableListActivity.class);
                            intent.putExtra("groupid",textnum.getText().toString());
                            intent.putExtra("groupowner",myname.getText().toString());
                            startActivity(intent);
                        default:
                            break;
                    }
                    return false;
                }
            });

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
                popupMenu.show();
                break;
        }
    }
}
