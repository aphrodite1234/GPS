package com.will.gps.base;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class RMessage {
    private String sender = null;
    private String receiver = null;
    private String content = null;
    private int state = 0;
    private Timestamp date = null;
    private String type = null;
    private List<Group> group = new ArrayList<Group>();

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String toString() {
        return "sender: " + sender + "\n" + "receiver: " + receiver + "\n" + "content: " + content;
    }

    public List<Group> getGroup() {
        return group;
    }

    public void setGroup(List<Group> group) {
        this.group = group;
    }
}