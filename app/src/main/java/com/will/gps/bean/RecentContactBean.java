package com.will.gps.bean;

/**
 * Created by wudeng on 2017/9/11.
 */

public class RecentContactBean {
    private Msg msg;
    private User mUser;
    private Group mGroup;

    public Msg getMsg() {
        return msg;
    }
    public void setMsg(Msg msg) {
        this.msg = msg;
    }

    public User getUser(){return mUser;}
    public void setUser(User user){mUser=user;}

    public Group getGroup(){return mGroup;}
    public void setGroup(Group group){mGroup=group;}
}
