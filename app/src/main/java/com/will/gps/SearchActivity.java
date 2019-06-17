package com.will.gps;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.will.gps.base.DBOpenHelper;
import com.will.gps.base.MySocket;
import com.will.gps.base.RMessage;
import com.will.gps.bean.Group;
import com.will.gps.bean.Signin;
import com.will.gps.map.AmapActivity;

/**
 * Created by MaiBenBen on 2019/4/22.
 */

public class SearchActivity extends Activity implements View.OnClickListener{
    private ImageView btn_back;
    private ImageView btn_search;
    private EditText editText;
    RMessage rMessage = new RMessage();
    Gson gson = new Gson();
    Group group = new Group();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        bindView();
        final DBOpenHelper dbOpenHelper = new DBOpenHelper(SearchActivity.this);
        ((MySocket)getApplication()).setHandler(new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                Gson gson = new Gson();
                Signin signin = new Signin();
                RMessage rMessage = gson.fromJson(msg.obj.toString(), RMessage.class);
                String type = rMessage.getType();
                switch (type){
                    case "搜索群":
                        if(!rMessage.getGroup().isEmpty()){
                            group=gson.fromJson(rMessage.getGroup().get(0),Group.class);
                            Intent intent=new Intent(SearchActivity.this,GroupInfoActivity.class);
                            intent.putExtra("groupname",group.getGroupname());
                            intent.putExtra("groupid",String.valueOf(group.getGroupid()));
                            intent.putExtra("groupowner",group.getGroupowner());
                            intent.putExtra("membernum",group.getMembernum());
                            intent.putExtra("ismember",rMessage.getContent());
                            startActivity(intent);
                            finish();
                        }else {
                            Toast.makeText(SearchActivity.this,"未查询到该群",Toast.LENGTH_SHORT).show();
                        }
                        break;
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
        btn_back=(ImageView)findViewById(R.id.search_back);
        btn_search=(ImageView)findViewById(R.id.search_search);
        editText=(EditText)findViewById(R.id.search_edit);

        btn_back.setOnClickListener(this);
        btn_search.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.search_back:
                finish();
                break;
            case R.id.search_search://查找群
                rMessage.setSenderphone(MySocket.user.getPhonenum());
                rMessage.setGroupid(Integer.parseInt(editText.getText().toString()));
                rMessage.setType("搜索群");
                ((MySocket)getApplication()).send(gson.toJson(rMessage));
                break;
        }
    }
}
