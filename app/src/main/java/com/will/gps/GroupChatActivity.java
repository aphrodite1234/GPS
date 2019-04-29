package com.will.gps;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.will.gps.adapter.ChatMessageAdapter;
import com.will.gps.base.ApplicationData;
import com.will.gps.base.MySocket;
import com.will.gps.base.RMessage;
import com.will.gps.bean.ChatEntity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import javax.crypto.spec.GCMParameterSpec;

/**
 * Created by MaiBenBen on 2019/4/27.
 */

public class GroupChatActivity extends Activity implements View.OnClickListener{
    private TextView mTitle;
    private int groupId;
    private String groupName;
    private ListView chatMeessageListView;
    private ChatMessageAdapter chatMessageAdapter;
    private Button sendButton;
    private LinearLayout linearLayout;
    //private ImageButton emotionButton;
    private EditText inputEdit;
    private List<ChatEntity> chatList;
    private Handler handler;
    private ImageView btn_back,btn_more;
    private Gson gson = new Gson();
    RMessage rMessage = new RMessage();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_group_chat);
        Intent intent = getIntent();
        groupName = intent.getStringExtra("groupName");
        groupId = intent.getIntExtra("groupId", 0);
//        initData(rMessage);
        initViews();
        initEvents();

        ((MySocket)getApplication()).setHandler(new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                rMessage = gson.fromJson(msg.obj.toString(),RMessage.class);
                if(rMessage.getType().equals("群消息")&&rMessage.getGroupid()==groupId){
                    initData(rMessage);
                }

            }
        });
    }

    private void initData(RMessage rMessage){
        ChatEntity chatEntity=new ChatEntity();
        chatEntity.setContent(rMessage.getContent());
        chatEntity.setSenderId(Integer.parseInt(rMessage.getSenderphone()));
        chatEntity.setSendTime(rMessage.getDate());
        chatList=new ArrayList<>(1);
        chatList.add(chatEntity);
    }

    protected void initViews() {
        // TODO Auto-generated method stub
        btn_back=(ImageView)findViewById(R.id.group_chat_back);
        btn_more=(ImageView)findViewById(R.id.group_chat_more);
        mTitle = (TextView) findViewById(R.id.group_chat_txt);
        //mTitleBarView.setCommonTitle(View.GONE, View.VISIBLE, View.GONE);
        mTitle.setText(groupName);
       chatMeessageListView = (ListView) findViewById(R.id.chat_Listview);
        sendButton = (Button) findViewById(R.id.chat_btn_send);
        linearLayout=(LinearLayout)findViewById(R.id.cb0ChatLayoutMsg);
        linearLayout.setBackgroundResource(R.drawable.bg_chatbar_textmode);
        //emotionButton = (ImageButton) findViewById(R.id.chat_btn_emote);
        inputEdit = (EditText) findViewById(R.id.chat_edit_input);

        btn_back.setOnClickListener(this);
    }

    protected void initEvents() {
        handler = new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 1:
                        chatMessageAdapter.notifyDataSetChanged();
                        chatMeessageListView.setSelection(chatList.size());
                        break;
                    default:
                        break;
                }
            }
        };
        /*ApplicationData.getInstance().setChatHandler(handler);
        chatList = ApplicationData.getInstance().getChatMessagesMap()
                .get(groupId);
        if(chatList == null){
            //chatList = ImDB.getInstance(GroupChatActivity.this).getChatMessage(friendId);
            ApplicationData.getInstance().getChatMessagesMap().put(groupId, chatList);
        }*/
        chatMessageAdapter = new ChatMessageAdapter(GroupChatActivity.this,chatList);
        chatMeessageListView.setAdapter(chatMessageAdapter);
        sendButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String content = inputEdit.getText().toString();
                inputEdit.setText("");
                RMessage rMessage = new RMessage();
                rMessage.setContent(content);
                rMessage.setSenderphone(MySocket.user.getPhonenum());
                rMessage.setSendername(MySocket.user.getUserName());
                rMessage.setGroupid(groupId);
                rMessage.setType("群消息");
                ChatEntity chatMessage = new ChatEntity();
                chatMessage.setContent(content);
                /*chatMessage.setSenderId(ApplicationData.getInstance()
                        .getUserInfo().getId());*/
                chatMessage.setReceiverId(groupId);
                chatMessage.setMessageType(ChatEntity.SEND);
                Date date = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("MM-dd hh:mm:ss");
                String sendTime = sdf.format(date);
                chatMessage.setSendTime(sendTime);
                rMessage.setDate(date);
                ((MySocket)getApplication()).send(gson.toJson(rMessage));
                chatList.add(chatMessage);
                chatMessageAdapter.notifyDataSetChanged();
                chatMeessageListView.setSelection(chatList.size());
                //UserAction.sendMessage(chatMessage);
                /*ImDB.getInstance(GroupChatActivity.this)
                        .saveChatMessage(chatMessage);*/
            }
        });
    }

   @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.group_chat_back:
            break;
            case R.id.group_chat_more:
                Intent i=new Intent(GroupChatActivity.this,GroupInfoActivity.class);
                startActivity(i);
                break;
        }
    }
}
