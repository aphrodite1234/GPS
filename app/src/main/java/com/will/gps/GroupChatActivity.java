package com.will.gps;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.will.gps.adapter.MsgAdapter;
import com.will.gps.bean.Msg;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MaiBenBen on 2019/4/23.
 */

public class GroupChatActivity extends Activity implements View.OnClickListener{

    private ImageView img_back;
    private ImageView img_info;
    private Button btn_send;
    private EditText input;
    private List<Msg> msgList = new ArrayList<>();
    private RecyclerView msgRecyclerView;
    private MsgAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_chat);
        initMsgs();
        bindView();

    }

    private void bindView(){
        img_back=(ImageView)findViewById(R.id.group_chat_back);
        img_info=(ImageView)findViewById(R.id.group_chat_more);
        input=(EditText)findViewById(R.id.group_chat_input);
        btn_send=(Button)findViewById(R.id.group_chat_send);
        msgRecyclerView=(RecyclerView)findViewById(R.id.msg_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        msgRecyclerView.setLayoutManager(layoutManager);
        adapter = new MsgAdapter(msgList);
        msgRecyclerView.setAdapter(adapter);

        img_back.setOnClickListener(this);
        img_info.setOnClickListener(this);
        btn_send.setOnClickListener(this);
    }

    private void initMsgs() {
        Msg msg1 = new Msg("Hello",Msg.TYPE_RECEIVED);
        msgList.add(msg1);
        Msg msg2 = new Msg("I'm John",Msg.TYPE_RECEIVED);
        msgList.add(msg2);
        Msg msg3 = new Msg("Hello",Msg.TYPE_SENT);
        msgList.add(msg3);
    }
    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.group_chat_back:
                finish();
                break;
            case R.id.group_chat_more:
                Intent intent = new Intent(GroupChatActivity.this, GroupInfoActivity.class);
                startActivity(intent);
                break;
            case R.id.group_chat_send:
                String content = input.getText().toString();
                if (!"".equals(content)) {
                    Msg msg = new Msg(content,Msg.TYPE_SENT);
                    msgList.add(msg);
                    adapter.notifyItemInserted(msgList.size()-1);
                    msgRecyclerView.scrollToPosition(msgList.size()-1);
                    input.setText("");
                }
                break;
        }
    }
}
