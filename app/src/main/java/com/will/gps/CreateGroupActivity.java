package com.will.gps;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.will.gps.base.MySocket;
import com.will.gps.base.RMessage;
import com.will.gps.bean.Group;

/**
 * Created by MaiBenBen on 2019/4/23.
 */

public class CreateGroupActivity extends Activity {

    private EditText groupname,groupID;
    private Button button;
    private String nameStr,idStr;
    RMessage rMessage = new RMessage();
    Gson gson = new Gson();
    Group group = new Group();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_create);
        bindView();
        ((MySocket)getApplication()).setHandler(new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                rMessage=gson.fromJson(msg.obj.toString(),RMessage.class);
                if(rMessage.getType().equals("创建群")){
                    if(rMessage.getContent().equals("true")){
                        idStr=String.valueOf(rMessage.getGroupid());
                        Toast.makeText(CreateGroupActivity.this,"创建成功",Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(CreateGroupActivity.this,GroupInfoActivity.class);
                        intent.putExtra("groupname",nameStr);
                        intent.putExtra("groupid",idStr);
                        intent.putExtra("type","create");
                        intent.putExtra("ismember","true");
                        startActivity(intent);
                        finish();
                    }else {
                        Toast.makeText(CreateGroupActivity.this,"创建失败",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void bindView(){
        groupname=(EditText)findViewById(R.id.group_create_name);
        //groupID=(EditText)findViewById(R.id.group_create_id);
        button=(Button)findViewById(R.id.group_create_btn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {//创建群
                nameStr= groupname.getText().toString().trim();
//                idStr=groupID.getText().toString().trim();
                if (null ==nameStr || TextUtils.isEmpty(nameStr)) {
                    Toast.makeText(CreateGroupActivity.this, "请输入群名称", Toast.LENGTH_SHORT).show();
                    return;
                }else if(nameStr.length()<3||nameStr.length()>16){
                    Toast.makeText(CreateGroupActivity.this, "群名称不合法", Toast.LENGTH_SHORT).show();
                    return;
                }
//                else if(null ==idStr || TextUtils.isEmpty(idStr)) {
//                    Toast.makeText(CreateGroupActivity.this, "请输入群ID", Toast.LENGTH_SHORT).show();
//                    return;
//                }else if(idStr.length()<3||idStr.length()>10){
//                    Toast.makeText(CreateGroupActivity.this, "群ID不合法", Toast.LENGTH_SHORT).show();
//                    return;
//                }
                group.setGroupname(nameStr);
                group.setGroupowner(MySocket.user.getPhonenum());
                group.setOwnername(MySocket.user.getUserName());
                rMessage.setType("创建群");
                rMessage.setContent(gson.toJson(group));
                ((MySocket)getApplication()).send(gson.toJson(rMessage));
                //创建群操作

            }
        });
    }
}
