package com.will.gps.bean;

import java.io.Serializable;
import java.text.DateFormat;

/**
 * Created by MaiBenBen on 2019/5/10.
 */

public class SignTableBean implements Serializable{
    private boolean state;//状态
    private int id;//id
    private int originator;//发起者
    private double longitude;//经度
    private double latitude;//维度
    private DateFormat time;//发起时间

    public boolean isState() {
        return state;
    }
    public void setState(boolean state) {
        this.state = state;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public int getOriginator() {
        return originator;
    }
    public void setOriginator(int originator) {
        this.originator = originator;
    }
    public double getLongitude() {
        return longitude;
    }
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
    public double getLatitude() {
        return latitude;
    }
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
    public DateFormat getTime() {
        return time;
    }
    public void setTime(DateFormat time) {
        this.time = time;
    }
}
