package com.will.gps.layout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
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

import com.will.gps.ChatActivity;
import com.will.gps.adapter.FriendMessageAdapter;
import com.will.gps.base.ApplicationData;
import com.will.gps.base.BaseDialog;
import com.will.gps.bean.MessageTabEntity;
import com.will.gps.view.SlideCutListView.RemoveDirection;
import com.will.gps.R;
import com.will.gps.view.SlideCutListView;
import com.will.gps.view.SlideCutListView.RemoveListener;
import java.util.List;

/**
 * Created by MaiBenBen on 2019/4/20.
 */
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context=getActivity();
        view = inflater.inflate(R.layout.activity_fragment_message,container,false);
        mMessageListView = (SlideCutListView) view.findViewById(R.id.message_list_listview);
        init();
        return view;
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
        mMessageEntityList = ApplicationData.getInstance().getMessageEntities();
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
                            Intent intent = new Intent(context,ChatActivity.class);
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
