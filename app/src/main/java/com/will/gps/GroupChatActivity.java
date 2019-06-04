package com.will.gps;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.will.gps.adapter.ChatMessageAdapter;
import com.will.gps.base.DBOpenHelper;
import com.will.gps.base.MySocket;
import com.will.gps.base.RMessage;
import com.will.gps.bean.ChatEntity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by MaiBenBen on 2019/4/27.
 */

public class GroupChatActivity extends Activity implements View.OnClickListener{
    private TextView mTitle;
    private int groupId,membernum;
    private String groupName,groupowner;
    private ListView chatMeessageListView;
    private ChatMessageAdapter chatMessageAdapter;
    private Button sendButton;
    //private ImageButton emotionButton;
    private EditText inputEdit;
    private List<ChatEntity> chatList=new ArrayList<>();
    private Handler handler;
    private ImageView btn_back,btn_more;
    private TextView signtime,signlocation;
    private Button signbutton;
    private RelativeLayout sign_title;//签到提示框
    boolean sign=true;//判断是否有签到活动,决定是否显示签到提示框
    boolean signed=false;//判断是否已经签到
    String ismember;
    RMessage rMessage = new RMessage();
    Gson gson = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_chat);
        Intent intent = getIntent();
        groupName = intent.getStringExtra("groupname");
        groupId = Integer.parseInt(intent.getStringExtra("groupid"));
        groupowner=intent.getStringExtra("groupowner");
        membernum=intent.getIntExtra("membernum",0);
        ismember=intent.getStringExtra("ismember");

        initViews();
        initEvents();
        final DBOpenHelper dbOpenHelper=new DBOpenHelper(GroupChatActivity.this);
        initData();
        ((MySocket)getApplication()).setHandler(new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                rMessage=gson.fromJson(msg.obj.toString(),RMessage.class);
                String type = rMessage.getType();
                if(type.equals("群消息")){
                    rMessage.setState(1);
                    dbOpenHelper.savemsg(dbOpenHelper,rMessage);
                    initData();
//                    ChatEntity chatMessage = new ChatEntity();
//                    chatMessage.setContent(rMessage.getContent());
//                    chatMessage.setSenderId(rMessage.getSenderphone());
//                    chatMessage.setSendTime(rMessage.getDate());
//                    chatMessage.setMessageType(ChatEntity.RECEIVE);
//                    chatList.add(chatMessage);
//                    chatMessageAdapter.notifyDataSetChanged();
//                    chatMeessageListView.setSelection(chatList.size());
                }
            }
        });
    }

    private void initData(){
        DBOpenHelper dbOpenHelper=new DBOpenHelper(GroupChatActivity.this);
        chatList=dbOpenHelper.searchmessage(dbOpenHelper,groupId);
        chatMessageAdapter.notifyDataSetChanged();
        chatMeessageListView.setSelection(chatList.size());
        dbOpenHelper.setMessage(dbOpenHelper,groupId);
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
        //emotionButton = (ImageButton) findViewById(R.id.chat_btn_emote);
        inputEdit = (EditText) findViewById(R.id.chat_edit_input);

        if(sign){//有签到活动则显示
            signtime=(TextView)findViewById(R.id.group_chat_signtime);//设置活动时间（可先不用）
            signlocation=(TextView)findViewById(R.id.group_chat_signlocation);//设置活动地点（可先不用）
            signbutton=(Button)findViewById(R.id.group_chat_signbutton);
            if(!signed){//没有签到
                signbutton.setOnClickListener(this);
            }
            else{
                signbutton.setText("已签到");
                signbutton.setClickable(false);
            }
        }
        else{//没有则隐藏
            sign_title=(RelativeLayout)findViewById(R.id.group_chat_signtitle);
            sign_title.setVisibility(View.GONE);
        }

        btn_back.setOnClickListener(this);
        btn_more.setOnClickListener(this);

    }

    protected void initEvents() {
//        handler = new Handler() {
//            public void handleMessage(Message msg) {
//                switch (msg.what) {
//                    case 1:
//                        chatMessageAdapter.notifyDataSetChanged();
//                        chatMeessageListView.setSelection(chatList.size());
//                        break;
//                    default:
//                        break;
//                }
//            }
//        };
//        ApplicationData.getInstance().setChatHandler(handler);
//        chatList = ApplicationData.getInstance().getChatMessagesMap()
//                .get(groupId);
//        if(chatList == null){
//            //chatList = ImDB.getInstance(GroupChatActivity.this).getChatMessage(friendId);
//            ApplicationData.getInstance().getChatMessagesMap().put(groupId, chatList);
//        }
        chatMessageAdapter = new ChatMessageAdapter(GroupChatActivity.this,chatList);
        chatMeessageListView.setAdapter(chatMessageAdapter);
        sendButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                DBOpenHelper dbOpenHelper=new DBOpenHelper(GroupChatActivity.this);
                String content = inputEdit.getText().toString();
                ChatEntity chatMessage = new ChatEntity();
                chatMessage.setContent(content);
                chatMessage.setSenderId(MySocket.user.getPhonenum());
                chatMessage.setSendername(MySocket.user.getUserName());
                chatMessage.setReceiverId(String.valueOf(groupId));
                chatMessage.setMessageType(ChatEntity.SEND);
                Date date = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("yy-MM-dd hh:mm:ss");
                String sendTime = sdf.format(date);
                chatMessage.setSendTime(sendTime);

                chatList.add(chatMessage);
                chatMessageAdapter.notifyDataSetChanged();
                chatMeessageListView.setSelection(chatList.size());
                //UserAction.sendMessage(chatMessage);
                /*ImDB.getInstance(GroupChatActivity.this)
                        .saveChatMessage(chatMessage);*/

                rMessage.setSenderphone(MySocket.user.getPhonenum());
                rMessage.setSendername(MySocket.user.getUserName());
                rMessage.setGroupid(groupId);
                rMessage.setContent(inputEdit.getText().toString());
                rMessage.setDate(sendTime);
                rMessage.setType("群消息");
                ((MySocket)getApplication()).send(gson.toJson(rMessage));
                dbOpenHelper.savemsg(dbOpenHelper,rMessage);
                inputEdit.setText("");
            }
        });
    }

   @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.group_chat_back:
                finish();
            break;
            case R.id.group_chat_more:
                Intent i=new Intent(GroupChatActivity.this,GroupInfoActivity.class);
                i.putExtra("groupname",groupName);
                i.putExtra("groupid",String.valueOf(groupId));
                i.putExtra("groupowner",groupowner);
                i.putExtra("membernum",membernum);
                i.putExtra("ismember",ismember);
                startActivity(i);
                break;
            case R.id.group_chat_signbutton:
                //签到操作
                break;
        }
    }
}
