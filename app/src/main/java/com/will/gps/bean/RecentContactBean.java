package com.will.gps.bean;

import com.netease.nimlib.sdk.msg.model.RecentContact;
import com.netease.nimlib.sdk.uinfo.UserInfoProvider;
import com.netease.nimlib.sdk.uinfo.model.UserInfo;
import com.will.gps.base.Group;
import com.will.gps.base.User;

/**
 * Created by wudeng on 2017/9/11.
 */

public class RecentContactBean {
    private RecentContact mRecentContact;
    private UserInfo mUserInfo;
    private User mUser;
    private Group mGroup;

    public RecentContact getRecentContact() {
        return mRecentContact;
    }

    public void setRecentContact(RecentContact recentContact) {
        mRecentContact = recentContact;
    }

    public UserInfo getUserInfo() {
        return mUserInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        mUserInfo = userInfo;
    }

    public User getUser(){return mUser;}
    public void setUser(User user){mUser=user;}

    public Group getGroup(){return mGroup;}
    public void setGroup(Group group){mGroup=group;}
}
