package com.will.gps.base;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;

import com.will.gps.bean.ChatEntity;
import com.will.gps.bean.MessageTabEntity;
import com.will.gps.bean.User;

import java.util.List;
import java.util.Map;

/**
 * Created by MaiBenBen on 2019/4/27.
 */

public class ApplicationData {

    private static ApplicationData mInitData;

    private User mUser;
    private boolean mIsReceived;
    private List<User> mFriendList;
    //private TranObject mReceivedMessage;
    private Map<Integer, Bitmap> mFriendPhotoMap;
    private Handler messageHandler;
    private Handler chatMessageHandler;
    private Handler friendListHandler;
    private Context mContext;
    private List<User> mFriendSearched;
    private Bitmap mUserPhoto;
    private List<MessageTabEntity> mMessageEntities;// messageFragment显示的列表
    private Map<Integer, List<ChatEntity>> mChatMessagesMap;

    public Map<Integer, List<ChatEntity>> getChatMessagesMap() {
        return mChatMessagesMap;
    }

    public void setChatMessagesMap(
            Map<Integer, List<ChatEntity>> mChatMessagesMap) {
        this.mChatMessagesMap = mChatMessagesMap;
    }
    public Map<Integer, Bitmap> getFriendPhotoMap() {
        return mFriendPhotoMap;
    }

    public Bitmap getUserPhoto() {
        return mUserPhoto;
    }
    public User getUserInfo() {
        return mUser;
    }
    public void setMessageHandler(Handler handler) {
        this.messageHandler = handler;
    }
    public List<MessageTabEntity> getMessageEntities() {
        return mMessageEntities;
    }
    public static ApplicationData getInstance() {
        if (mInitData == null) {
            mInitData = new ApplicationData();
        }
        return mInitData;
    }
    public void setChatHandler(Handler handler) {
        this.chatMessageHandler = handler;
    }
}
