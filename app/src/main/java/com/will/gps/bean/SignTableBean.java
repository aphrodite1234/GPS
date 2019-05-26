package com.will.gps.bean;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by MaiBenBen on 2019/5/10.
 */

public class SignTableBean implements Serializable{
    private boolean state;//状态
    private int id;//id
    private String originator;//发起者
    private double longitude;//经度
    private double latitude;//纬度
    private int region;
    private Date time;//发起时间

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
    public String getOriginator() {
        return originator;
    }
    public void setOriginator(String originator) {
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
    public int getRegion() {
        return region;
    }
    public void setRegion(int region) {
        this.region = region;
    }
    public Date getTime() {
        return time;
    }
    public void setTime(Date time) {
        this.time = time;
    }
}
