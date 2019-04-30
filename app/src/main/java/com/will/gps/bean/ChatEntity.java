package com.will.gps.bean;

/**
 * Created by MaiBenBen on 2019/4/27.
 */
import java.io.Serializable;

public class ChatEntity implements Serializable{
    public static final int  RECEIVE = 0;
    public static final int SEND = 1;
    private String senderId;
    private String receiverId;
    private String sendDate;
    private int messageType;
    private String content;
    public String getSenderId() {
        return senderId;
    }
    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }
    public String getReceiverId() {
        return receiverId;
    }
    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public int getMessageType() {
        return messageType;
    }
    public void setMessageType(int messageType) {
        this.messageType = messageType;
    }
    public String getSendTime() {
        return sendDate;
    }
    public void setSendTime(String sendDate) {
        this.sendDate = sendDate;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
}