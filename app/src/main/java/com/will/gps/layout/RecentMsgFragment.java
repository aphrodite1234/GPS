package com.will.gps.layout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.app.Fragment;
import android.widget.AdapterView;
import android.widget.Toast;

import com.will.gps.GroupChatActivity;
import com.will.gps.adapter.FriendMessageAdapter;
import com.will.gps.base.ApplicationData;
import com.will.gps.base.BaseDialog;
import com.will.gps.bean.MessageTabEntity;
import com.will.gps.view.SlideCutListView.RemoveDirection;
import com.will.gps.R;
import com.will.gps.view.SlideCutListView;
import com.will.gps.view.SlideCutListView.RemoveListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MaiBenBen on 2019/4/20.
 */
@SuppressLint("ValidFragment")
public class RecentMsgFragment extends Fragment implements RemoveListener{
    private View view;
    private Context context;
    private SlideCutListView mMessageListView;
    private BaseDialog mDialog;
    private Handler handler;
    private int mPosition;
    private FriendMessageAdapter adapter;
    private List<MessageTabEntity> mMessageEntityList;
    private MessageTabEntity chooseMessageEntity;
    private MessageTabEntity m1,m2,m3;//测试数据

    @SuppressLint("ValidFragment")
    public RecentMsgFragment(List<MessageTabEntity> List){
        mMessageEntityList=List;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context=getActivity();
        view = inflater.inflate(R.layout.activity_fragment_message,container,false);
        mMessageListView = (SlideCutListView) view.findViewById(R.id.message_list_listview);
        initData();
        init();
        return view;
    }
    private void initData(){//初始化测试数据
        m1=new MessageTabEntity();
        m2=new MessageTabEntity();
        m3=new MessageTabEntity();

        m1.setName("张三");
        m1.setContent("你好！");
        m1.setSenderId(123);
        m1.setSendTime("2003-05-07 18:00");

        m2.setName("李四");
        m2.setContent("你好！1");
        m2.setSenderId(123456);
        m2.setSendTime("2019-04-27 8:00");

        m3.setName("王五");
        m3.setContent("你好！2");
        m3.setSenderId(123456789);
        m3.setSendTime("2023-05-07 00:00");

        mMessageEntityList=new ArrayList<MessageTabEntity>(3);
        mMessageEntityList.add(0,m1);
        mMessageEntityList.add(1,m2);
        mMessageEntityList.add(2,m3);
    }
    private void init() {
        mMessageListView.setRemoveListener(this);
        //initDialog();
        handler = new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 1:
                        adapter.notifyDataSetChanged();
                        mMessageListView.setSelection(mMessageEntityList.size());
                        break;
                    default:
                        break;
                }
            }
        };
        ApplicationData.getInstance().setMessageHandler(handler);
        //mMessageEntityList = ApplicationData.getInstance().getMessageEntities();
        mMessageListView.setSelection(mMessageEntityList.size());
        adapter = new FriendMessageAdapter(context, mMessageEntityList);
        mMessageListView.setAdapter(adapter);
        mMessageListView
                .setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
                        chooseMessageEntity = mMessageEntityList.get(position);
                        chooseMessageEntity.setUnReadCount(0);
                        adapter.notifyDataSetChanged();
                        //ImDB.getInstance(mContext).updateMessages(chooseMessageEntity);
                        //mPosition = position;
                        if (chooseMessageEntity.getMessageType() == MessageTabEntity.MAKE_FRIEND_REQUEST)
                            Toast.makeText(context,"接受好友功能未实现！",Toast.LENGTH_SHORT).show();
                            //mDialog.show();
                        else if (chooseMessageEntity.getMessageType() == MessageTabEntity.MAKE_FRIEND_RESPONSE_ACCEPT) {

                        }else {
                            Intent intent = new Intent(context,GroupChatActivity.class);
                            intent.putExtra("friendName", chooseMessageEntity.getName());
                            intent.putExtra("friendId", chooseMessageEntity.getSenderId());
                            startActivity(intent);
                        }
                    }
                });
    }
    /*private void initDialog() {//初始化dialog控件
        mDialog = BaseDialog.getDialog(mContext, "是否接受好友请求?", "", "接受",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                        UserAction.sendFriendRequest(
                                Result.FRIEND_REQUEST_RESPONSE_ACCEPT,
                                chooseMessageEntity.getSenderId());
                        mMessageEntityList.remove(mPosition);
                        ImDB.getInstance(mContext).deleteMessage(
                                chooseMessageEntity);

                    }
                }, "拒绝", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        UserAction.sendFriendRequest(
                                Result.FRIEND_REQUEST_RESPONSE_REJECT,
                                chooseMessageEntity.getSenderId());
                        mMessageEntityList.remove(mPosition);
                        ImDB.getInstance(mContext).deleteMessage(
                                chooseMessageEntity);
                        adapter.notifyDataSetChanged();
                    }
                });
        mDialog.setButton1Background(R.drawable.btn_default_popsubmit);
    }*/

    // 滑动删除之后的回调方法
    @Override
    public void removeItem(RemoveDirection direction, int position) {
        MessageTabEntity temp = mMessageEntityList.get(position);
        mMessageEntityList.remove(position);
        adapter.notifyDataSetChanged();
        switch (direction) {
            default:
                //ImDB.getInstance(mContext).deleteMessage(temp);
                break;
        }
    }
}
