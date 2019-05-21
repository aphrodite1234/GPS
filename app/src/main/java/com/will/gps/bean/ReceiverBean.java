package com.will.gps.bean;

/**
 * Created by MaiBenBen on 2019/5/20.
 */

public class ReceiverBean {
    private String id;
    private String realname;
    private double rlongitude;
    private double rlatitude;
    private boolean done;
    private boolean result;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public double getRlongitude() {
        return rlongitude;
    }

    public void setRlongitude(double rlongitude) {
        this.rlongitude = rlongitude;
    }

    public double getRlatitude() {
        return rlatitude;
    }

    public void setRlatitude(double rlatitude) {
        this.rlatitude = rlatitude;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }
}
