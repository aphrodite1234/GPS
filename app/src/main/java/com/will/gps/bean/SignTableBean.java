package com.will.gps.bean;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by MaiBenBen on 2019/5/10.
 */

public class SignTableBean implements Serializable{
    private int state;//状态
    private int id;//id
    private String originator;//发起者
    private String longitude;//经度
    private String latitude;//纬度
    private int region;
    private String time;//发起时间
    private int groupid;
    private String content;

    public int getState() {
        return state;
    }
    public void setState(int state) {
        this.state = state;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getOriginator() {
        return originator;
    }
    public void setOriginator(String originator) {
        this.originator = originator;
    }
    public String getLongitude() {
        return longitude;
    }
    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
    public String getLatitude() {
        return latitude;
    }
    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }
    public int getRegion() {
        return region;
    }
    public void setRegion(int region) {
        this.region = region;
    }
    public String getTime() {
        return time;
    }
    public void setTime(String time) {
        this.time = time;
    }
    public int getGroupid() {
        return groupid;
    }
    public void setGroupid(int groupid) {
        this.groupid = groupid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String conttent) {
        this.content = conttent;
    }
}
